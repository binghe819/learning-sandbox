package com.binghe.tsocket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * TSocket (non-framed) 방식의 Thrift 메시지를 디코딩하는 Handler
 * 
 * TSocket은 메시지 길이 헤더가 없으므로, Thrift Binary Protocol 구조를 파싱해서
 * 완전한 메시지가 도착했는지 확인해야 함.
 * 
 * Thrift Binary Protocol 구조:
 * - Version (4 bytes): 0x80010000 + message type
 * - Method name length (4 bytes)
 * - Method name (variable bytes)
 * - Sequence ID (4 bytes)
 * - Message body (struct fields, variable length)
 * - Stop field (1 byte: 0x00)
 */
public class ThriftTSocketDecoder extends ByteToMessageDecoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThriftTSocketDecoder.class);
    
    // Thrift Binary Protocol strict version bit
    private static final int VERSION_MASK = 0xffff0000;
    private static final int VERSION_1 = 0x80010000;
    
    // 최소 헤더 크기: version(4) + name_length(4) + name(최소 1) + seqid(4) = 13 bytes
    private static final int MIN_HEADER_SIZE = 13;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 읽기 시작 위치 저장
        int startReaderIndex = in.readerIndex();
        
        // 최소 헤더 크기 확인
        if (in.readableBytes() < MIN_HEADER_SIZE) {
            LOGGER.debug("Not enough data for header. Available: {}, Required: {}", 
                    in.readableBytes(), MIN_HEADER_SIZE);
            return; // 더 많은 데이터 대기
        }
        
        try {
            // 완전한 메시지인지 확인
            Integer messageLength = tryParseMessage(in);
            
            if (messageLength == null) {
                // 메시지가 불완전함, readerIndex 복원하고 더 많은 데이터 대기
                in.readerIndex(startReaderIndex);
                LOGGER.debug("Incomplete message, waiting for more data. Current buffer size: {}", 
                        in.readableBytes());
                return;
            }
            
            // 완전한 메시지를 찾음
            in.readerIndex(startReaderIndex); // readerIndex 복원
            
            // 메시지 전체를 읽어서 다음 handler로 전달
            ByteBuf message = in.readRetainedSlice(messageLength);
            out.add(message);
            
            LOGGER.debug("Decoded complete Thrift message: {} bytes", messageLength);
            
        } catch (Exception e) {
            // 파싱 중 에러 발생 - 잘못된 메시지일 수 있음
            LOGGER.error("Error parsing Thrift message", e);
            in.readerIndex(startReaderIndex);
            // 에러 발생 시 버퍼를 비워서 연결 해제. 재연결 유도.
            ctx.close();
            throw e;
        }
    }
    
    /**
     * Thrift 메시지를 파싱해서 완전한 메시지인지 확인
     * 
     * @param buffer 파싱할 버퍼
     * @return 완전한 메시지면 메시지 길이 반환, 불완전하면 null 반환
     */
    private Integer tryParseMessage(ByteBuf buffer) {
        int startIndex = buffer.readerIndex();
        
        try {
            // 1. Version과 Message Type 읽기 (4 bytes)
            if (buffer.readableBytes() < 4) {
                return null;
            }
            int version = buffer.readInt();
            
            // Strict mode 확인
            if ((version & VERSION_MASK) != VERSION_1) {
                LOGGER.error("Invalid Thrift version: 0x{}", Integer.toHexString(version));
                throw new IllegalArgumentException("Invalid Thrift protocol version");
            }
            
            // 2. Method name length 읽기 (4 bytes)
            if (buffer.readableBytes() < 4) {
                return null;
            }
            int nameLength = buffer.readInt();
            
            // Method name 길이 검증 (비정상적으로 큰 값 방지)
            if (nameLength < 0 || nameLength > 1024) {
                throw new IllegalArgumentException("Invalid method name length: " + nameLength);
            }
            
            // 3. Method name 읽기 (nameLength bytes)
            if (buffer.readableBytes() < nameLength) {
                return null;
            }
            buffer.skipBytes(nameLength); // method name 건너뛰기
            
            // 4. Sequence ID 읽기 (4 bytes)
            if (buffer.readableBytes() < 4) {
                return null;
            }
            buffer.readInt(); // seqId 건너뛰기
            
            // 5. Message body (struct fields) 읽기
            // struct의 모든 필드를 읽어야 함
            if (!trySkipStruct(buffer)) {
                return null;
            }
            
            // 메시지를 완전히 읽었으면 총 길이 반환
            int messageLength = buffer.readerIndex() - startIndex;
            
            LOGGER.debug("Parsed complete message. Length: {}", messageLength);
            
            return messageLength;
            
        } catch (Exception e) {
            // 파싱 실패 - 데이터 부족이거나 잘못된 메시지
            LOGGER.debug("Failed to parse message", e);
            return null;
        } finally {
            // readerIndex 복원
            buffer.readerIndex(startIndex);
        }
    }
    
    /**
     * Thrift struct를 건너뛰기 (필드들을 읽어서 STOP field까지 도달)
     * 
     * @param buffer 읽을 버퍼
     * @return 완전히 읽었으면 true, 데이터가 부족하면 false
     */
    private boolean trySkipStruct(ByteBuf buffer) {
        while (true) {
            // Field type 읽기 (1 byte)
            if (buffer.readableBytes() < 1) {
                return false;
            }
            
            byte fieldType = buffer.readByte();
            
            // STOP field (0x00)
            if (fieldType == 0) {
                return true; // struct 끝
            }
            
            // Field ID 읽기 (2 bytes)
            if (buffer.readableBytes() < 2) {
                return false;
            }
            buffer.readShort();
            
            // Field value 건너뛰기
            if (!trySkipFieldValue(buffer, fieldType)) {
                return false;
            }
        }
    }
    
    /**
     * Field value를 건너뛰기
     * 
     * @param buffer 읽을 버퍼
     * @param fieldType 필드 타입
     * @return 성공하면 true, 데이터 부족하면 false
     */
    private boolean trySkipFieldValue(ByteBuf buffer, byte fieldType) {
        switch (fieldType) {
            case 2: // BOOL (1 byte)
                if (buffer.readableBytes() < 1) return false;
                buffer.readByte();
                return true;
                
            case 3: // BYTE (1 byte)
                if (buffer.readableBytes() < 1) return false;
                buffer.readByte();
                return true;
                
            case 4: // DOUBLE (8 bytes)
                if (buffer.readableBytes() < 8) return false;
                buffer.skipBytes(8);
                return true;
                
            case 6: // I16 (2 bytes)
                if (buffer.readableBytes() < 2) return false;
                buffer.readShort();
                return true;
                
            case 8: // I32 (4 bytes)
                if (buffer.readableBytes() < 4) return false;
                buffer.readInt();
                return true;
                
            case 10: // I64 (8 bytes)
                if (buffer.readableBytes() < 8) return false;
                buffer.skipBytes(8);
                return true;
                
            case 11: // STRING or BINARY
                // length(4) + data(length)
                if (buffer.readableBytes() < 4) return false;
                int length = buffer.readInt();
                if (length < 0 || length > 10 * 1024 * 1024) { // 10MB 제한
                    throw new IllegalArgumentException("String/Binary length too large: " + length);
                }
                if (buffer.readableBytes() < length) return false;
                buffer.skipBytes(length);
                return true;
                
            case 12: // STRUCT
                return trySkipStruct(buffer);
                
            case 13: // MAP
                // key_type(1) + value_type(1) + size(4) + entries
                if (buffer.readableBytes() < 6) return false;
                byte keyType = buffer.readByte();
                byte valueType = buffer.readByte();
                int mapSize = buffer.readInt();
                if (mapSize < 0 || mapSize > 100000) {
                    throw new IllegalArgumentException("Map size too large: " + mapSize);
                }
                for (int i = 0; i < mapSize; i++) {
                    if (!trySkipFieldValue(buffer, keyType)) return false;
                    if (!trySkipFieldValue(buffer, valueType)) return false;
                }
                return true;
                
            case 14: // SET
            case 15: // LIST
                // element_type(1) + size(4) + elements
                if (buffer.readableBytes() < 5) return false;
                byte elemType = buffer.readByte();
                int size = buffer.readInt();
                if (size < 0 || size > 100000) {
                    throw new IllegalArgumentException("Collection size too large: " + size);
                }
                for (int i = 0; i < size; i++) {
                    if (!trySkipFieldValue(buffer, elemType)) return false;
                }
                return true;
                
            default:
                throw new IllegalArgumentException("Unknown field type: " + fieldType);
        }
    }
}

