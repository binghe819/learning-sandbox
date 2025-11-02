package com.binghe;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.IllegalReferenceCountException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ByteBuf의 핵심 개념을 이해하기 위한 테스트 코드
 * 
 * ByteBuf는 Netty에서 제공하는 바이트 컨테이너로, Java NIO의 ByteBuffer를 개선한 버전입니다.
 * 주요 특징:
 * 1. Reference Counting을 통한 메모리 관리
 * 2. Reader/Writer Index를 통한 편리한 읽기/쓰기
 * 3. Zero-copy를 통한 성능 최적화
 */
public class ByteBufTest {

    /**
     * 테스트 1: Reference Counting (참조 카운팅)
     * 
     * ByteBuf는 참조 카운팅을 사용하여 메모리를 관리합니다.
     * - 생성 시 refCnt는 1입니다.
     * - retain()을 호출하면 refCnt가 증가합니다.
     * - release()를 호출하면 refCnt가 감소합니다.
     * - refCnt가 0이 되면 메모리가 해제됩니다.
     */
    @Test
    void testReferenceCountingBasics() {
        // 1. ByteBuf 생성 시 참조 카운트는 1
        ByteBuf buffer = Unpooled.buffer(10);
        assertEquals(1, buffer.refCnt(), "생성 직후 refCnt는 1이어야 합니다");
        
        // 2. retain()으로 참조 카운트 증가
        buffer.retain();
        assertEquals(2, buffer.refCnt(), "retain() 호출 후 refCnt는 2가 되어야 합니다");
        
        // 3. release()로 참조 카운트 감소
        buffer.release();
        assertEquals(1, buffer.refCnt(), "release() 호출 후 refCnt는 1로 감소해야 합니다");
        
        // 4. 마지막 release() - 메모리 해제
        boolean released = buffer.release();
        assertTrue(released, "마지막 release()는 true를 반환해야 합니다");
        assertEquals(0, buffer.refCnt(), "모든 release() 후 refCnt는 0이 되어야 합니다");
        
        // 5. refCnt가 0이 된 후에는 버퍼를 사용할 수 없음
        assertThrows(IllegalReferenceCountException.class, () -> {
            buffer.writeByte(1);
        }, "release된 버퍼는 사용할 수 없어야 합니다");
    }

    /**
     * 테스트 2: 메모리 누수 방지
     * 
     * ByteBuf를 사용한 후 반드시 release()를 호출해야 합니다.
     * 그렇지 않으면 메모리 누수가 발생합니다.
     */
    @Test
    void testMemoryLeakPrevention() {
        ByteBuf buffer = Unpooled.buffer(10);
        
        try {
            // 버퍼 사용
            buffer.writeInt(42);
            int value = buffer.readInt();
            assertEquals(42, value);
            
        } finally {
            // try-finally를 사용하여 반드시 release 호출
            buffer.release();
        }
        
        assertEquals(0, buffer.refCnt(), "finally 블록에서 release가 호출되어야 합니다");
    }

    /**
     * 테스트 3: Reader/Writer Index
     * 
     * ByteBuf는 readerIndex와 writerIndex를 가지고 있습니다.
     * - writerIndex: 다음에 쓸 위치
     * - readerIndex: 다음에 읽을 위치
     * - 0 <= readerIndex <= writerIndex <= capacity
     */
    @Test
    void testReaderWriterIndex() {
        ByteBuf buffer = Unpooled.buffer(10);
        
        try {
            // 1. 초기 상태
            assertEquals(0, buffer.readerIndex(), "초기 readerIndex는 0");
            assertEquals(0, buffer.writerIndex(), "초기 writerIndex는 0");
            
            // 2. 데이터 쓰기 - writerIndex가 증가
            buffer.writeInt(100);
            assertEquals(0, buffer.readerIndex(), "쓰기는 readerIndex에 영향을 주지 않습니다");
            assertEquals(4, buffer.writerIndex(), "int 쓰기 후 writerIndex는 4 증가");
            
            // 3. 데이터 읽기 - readerIndex가 증가
            int value = buffer.readInt();
            assertEquals(100, value);
            assertEquals(4, buffer.readerIndex(), "int 읽기 후 readerIndex는 4 증가");
            assertEquals(4, buffer.writerIndex(), "읽기는 writerIndex에 영향을 주지 않습니다");
            
            // 4. Index 수동 설정 (readerIndex를 먼저 설정해야 함)
            buffer.readerIndex(0);
            buffer.writerIndex(0);
            assertEquals(0, buffer.readerIndex());
            assertEquals(0, buffer.writerIndex());
            
        } finally {
            buffer.release();
        }
    }

    /**
     * 테스트 4: Readable/Writable Bytes
     * 
     * - readableBytes(): writerIndex - readerIndex
     * - writableBytes(): capacity - writerIndex
     * - isReadable(): readableBytes() > 0
     * - isWritable(): writableBytes() > 0
     */
    @Test
    void testReadableWritableBytes() {
        ByteBuf buffer = Unpooled.buffer(10); // capacity = 10
        
        try {
            // 1. 초기 상태
            assertEquals(0, buffer.readableBytes(), "초기에는 읽을 데이터가 없습니다");
            assertEquals(10, buffer.writableBytes(), "초기에는 10바이트를 쓸 수 있습니다");
            assertFalse(buffer.isReadable(), "읽을 데이터가 없습니다");
            assertTrue(buffer.isWritable(), "쓸 공간이 있습니다");
            
            // 2. 4바이트 쓰기
            buffer.writeInt(42);
            assertEquals(4, buffer.readableBytes(), "4바이트를 읽을 수 있습니다");
            assertEquals(6, buffer.writableBytes(), "6바이트를 쓸 수 있습니다");
            assertTrue(buffer.isReadable(), "읽을 데이터가 있습니다");
            
            // 3. 2바이트 읽기
            buffer.readShort();
            assertEquals(2, buffer.readableBytes(), "2바이트를 읽을 수 있습니다");
            assertEquals(6, buffer.writableBytes(), "쓸 수 있는 공간은 동일합니다");
            
        } finally {
            buffer.release();
        }
    }

    /**
     * 테스트 5: 다양한 Read/Write 메서드
     * 
     * ByteBuf는 다양한 데이터 타입을 읽고 쓸 수 있습니다.
     */
    @Test
    void testReadWriteMethods() {
        ByteBuf buffer = Unpooled.buffer(100);
        
        try {
            // 1. Byte 쓰기/읽기
            buffer.writeByte(10);
            assertEquals(10, buffer.readByte());
            
            // 2. Short 쓰기/읽기
            buffer.writeShort(1000);
            assertEquals(1000, buffer.readShort());
            
            // 3. Int 쓰기/읽기
            buffer.writeInt(100000);
            assertEquals(100000, buffer.readInt());
            
            // 4. Long 쓰기/읽기
            buffer.writeLong(10000000000L);
            assertEquals(10000000000L, buffer.readLong());
            
            // 5. Boolean 쓰기/읽기
            buffer.writeBoolean(true);
            assertTrue(buffer.readBoolean());
            
            // 6. Byte 배열 쓰기/읽기
            byte[] data = "Hello".getBytes();
            buffer.writeBytes(data);
            byte[] read = new byte[5];
            buffer.readBytes(read);
            assertArrayEquals(data, read);
            
        } finally {
            buffer.release();
        }
    }

    /**
     * 테스트 6: Get/Set 메서드 (Index를 변경하지 않음)
     * 
     * get/set 메서드는 index를 이동시키지 않습니다.
     * read/write 메서드는 index를 이동시킵니다.
     */
    @Test
    void testGetSetVsReadWrite() {
        ByteBuf buffer = Unpooled.buffer(10);
        
        try {
            // 1. setInt는 writerIndex를 변경하지 않음
            buffer.setInt(0, 100);
            assertEquals(0, buffer.writerIndex(), "setInt는 writerIndex를 변경하지 않습니다");
            
            // 2. writerIndex를 수동으로 설정
            buffer.writerIndex(4);
            
            // 3. getInt는 readerIndex를 변경하지 않음
            int value1 = buffer.getInt(0);
            assertEquals(100, value1);
            assertEquals(0, buffer.readerIndex(), "getInt는 readerIndex를 변경하지 않습니다");
            
            // 4. getInt는 여러 번 호출 가능
            int value2 = buffer.getInt(0);
            assertEquals(100, value2);
            
            // 5. readInt는 readerIndex를 변경
            int value3 = buffer.readInt();
            assertEquals(100, value3);
            assertEquals(4, buffer.readerIndex(), "readInt는 readerIndex를 변경합니다");
            
        } finally {
            buffer.release();
        }
    }

    /**
     * 테스트 7: Slice (슬라이스)
     * 
     * slice()는 현재 버퍼의 일부를 공유하는 새로운 ByteBuf를 만듭니다.
     * - 원본과 메모리를 공유합니다 (Zero-copy)
     * - 독립적인 readerIndex, writerIndex를 가집니다
     * - 참조 카운트를 공유합니다
     */
    @Test
    void testSlice() {
        ByteBuf buffer = Unpooled.buffer(10);
        
        try {
            // 1. 데이터 쓰기
            buffer.writeInt(100);
            buffer.writeInt(200);
            buffer.writeInt(300);
            
            // 2. readerIndex를 4로 이동 (첫 번째 int 건너뛰기)
            buffer.readerIndex(4);
            
            // 3. Slice 생성 (현재 readerIndex부터 readableBytes만큼)
            ByteBuf slice = buffer.slice();
            
            // 4. Slice는 독립적인 인덱스를 가짐
            assertEquals(0, slice.readerIndex(), "Slice의 readerIndex는 0부터 시작");
            assertEquals(8, slice.readableBytes(), "Slice는 8바이트를 읽을 수 있습니다");
            
            // 5. Slice로 데이터 읽기
            assertEquals(200, slice.readInt());
            assertEquals(300, slice.readInt());
            
            // 6. 원본과 메모리를 공유하는지 확인
            buffer.setInt(4, 999);  // 원본 변경
            slice.readerIndex(0);   // Slice 인덱스 리셋
            assertEquals(999, slice.readInt(), "Slice는 원본과 메모리를 공유합니다");
            
            // 7. 참조 카운트 공유 확인
            assertEquals(buffer.refCnt(), slice.refCnt(), "Slice는 원본과 refCnt를 공유합니다");
            
        } finally {
            buffer.release();
        }
    }

    /**
     * 테스트 8: Duplicate (복제)
     * 
     * duplicate()는 전체 버퍼를 공유하는 새로운 ByteBuf를 만듭니다.
     * - slice와 유사하지만 전체 버퍼를 공유합니다
     * - 메모리를 공유합니다 (Zero-copy)
     * - 독립적인 인덱스를 가집니다
     */
    @Test
    void testDuplicate() {
        ByteBuf buffer = Unpooled.buffer(10);
        
        try {
            // 1. 데이터 쓰기 및 읽기
            buffer.writeInt(100);
            buffer.writeInt(200);
            buffer.readInt(); // readerIndex를 4로 이동
            
            // 2. Duplicate 생성
            ByteBuf duplicate = buffer.duplicate();
            
            // 3. 인덱스는 복사되지만 독립적
            assertEquals(buffer.readerIndex(), duplicate.readerIndex());
            assertEquals(buffer.writerIndex(), duplicate.writerIndex());
            
            // duplicate의 인덱스 변경
            duplicate.readerIndex(0);
            assertEquals(0, duplicate.readerIndex());
            assertEquals(4, buffer.readerIndex(), "원본의 인덱스는 변경되지 않습니다");
            
            // 4. 메모리는 공유
            buffer.setInt(0, 999);
            assertEquals(999, duplicate.getInt(0), "Duplicate는 원본과 메모리를 공유합니다");
            
        } finally {
            buffer.release();
        }
    }

    /**
     * 테스트 9: Copy (복사)
     * 
     * copy()는 새로운 버퍼를 만들고 데이터를 복사합니다.
     * - 메모리를 공유하지 않습니다
     * - 독립적인 참조 카운트를 가집니다
     * - 원본과 완전히 독립적입니다
     */
    @Test
    void testCopy() {
        ByteBuf buffer = Unpooled.buffer(10);
        
        try {
            // 1. 데이터 쓰기
            buffer.writeInt(100);
            buffer.writeInt(200);
            
            // 2. Copy 생성
            ByteBuf copy = buffer.copy();
            
            try {
                // 3. 독립적인 참조 카운트
                assertEquals(1, buffer.refCnt());
                assertEquals(1, copy.refCnt());
                buffer.retain();
                assertEquals(2, buffer.refCnt());
                assertEquals(1, copy.refCnt(), "Copy는 독립적인 refCnt를 가집니다");
                buffer.release();
                
                // 4. 메모리를 공유하지 않음
                buffer.setInt(0, 999);
                assertEquals(999, buffer.getInt(0));
                assertEquals(100, copy.getInt(0), "Copy는 원본과 메모리를 공유하지 않습니다");
                
            } finally {
                copy.release();
            }
        } finally {
            buffer.release();
        }
    }

    /**
     * 테스트 10: Clear (초기화)
     * 
     * clear()는 readerIndex와 writerIndex를 0으로 리셋합니다.
     * - 실제 데이터는 지우지 않습니다
     * - 단순히 인덱스만 리셋합니다
     */
    @Test
    void testClear() {
        ByteBuf buffer = Unpooled.buffer(10);
        
        try {
            // 1. 데이터 쓰기 및 읽기
            buffer.writeInt(100);
            buffer.readShort();
            assertEquals(2, buffer.readerIndex());
            assertEquals(4, buffer.writerIndex());
            
            // 2. Clear
            buffer.clear();
            assertEquals(0, buffer.readerIndex(), "clear() 후 readerIndex는 0");
            assertEquals(0, buffer.writerIndex(), "clear() 후 writerIndex는 0");
            
            // 3. 실제 데이터는 그대로 있음
            assertEquals(100, buffer.getInt(0), "clear()는 데이터를 지우지 않습니다");
            
        } finally {
            buffer.release();
        }
    }

    /**
     * 테스트 11: Discard Read Bytes (읽은 바이트 버리기)
     * 
     * discardReadBytes()는 읽은 바이트를 버리고 버퍼를 압축합니다.
     * - readerIndex 이전의 바이트를 버립니다
     * - 읽지 않은 바이트를 버퍼의 앞으로 이동시킵니다
     */
    @Test
    void testDiscardReadBytes() {
        ByteBuf buffer = Unpooled.buffer(10);
        
        try {
            // 1. 데이터 쓰기
            buffer.writeInt(100);
            buffer.writeInt(200);
            buffer.writeInt(300);
            
            // 2. 첫 번째 int 읽기
            assertEquals(100, buffer.readInt());
            assertEquals(4, buffer.readerIndex());
            assertEquals(12, buffer.writerIndex());
            
            // 3. 읽은 바이트 버리기
            buffer.discardReadBytes();
            assertEquals(0, buffer.readerIndex(), "discardReadBytes() 후 readerIndex는 0");
            assertEquals(8, buffer.writerIndex(), "writerIndex는 readableBytes만큼 감소");
            
            // 4. 데이터는 그대로
            assertEquals(200, buffer.readInt());
            assertEquals(300, buffer.readInt());
            
        } finally {
            buffer.release();
        }
    }

    /**
     * 테스트 12: 여러 참조를 가진 경우
     * 
     * 여러 곳에서 같은 ByteBuf를 참조할 때의 동작을 이해합니다.
     */
    @Test
    void testMultipleReferences() {
        ByteBuf buffer = Unpooled.buffer(10);
        
        // 1. 첫 번째 참조
        assertEquals(1, buffer.refCnt());
        
        // 2. 두 번째 참조 생성
        ByteBuf ref1 = buffer.retain();
        assertEquals(2, buffer.refCnt());
        assertSame(buffer, ref1, "retain()은 같은 객체를 반환합니다");
        
        // 3. 세 번째 참조 생성
        ByteBuf ref2 = buffer.retain();
        assertEquals(3, buffer.refCnt());
        
        // 4. 참조 하나씩 해제
        ref1.release();
        assertEquals(2, buffer.refCnt());
        
        ref2.release();
        assertEquals(1, buffer.refCnt());
        
        buffer.release();
        assertEquals(0, buffer.refCnt());
    }

    /**
     * 테스트 13: 용량 관리
     * 
     * ByteBuf는 필요에 따라 용량을 조정할 수 있습니다.
     */
    @Test
    void testCapacityManagement() {
        ByteBuf buffer = Unpooled.buffer(10); // 초기 용량 10
        
        try {
            // 1. 초기 용량
            assertEquals(10, buffer.capacity());
            assertEquals(10, buffer.writableBytes());
            
            // 2. 용량 확장
            buffer.capacity(20);
            assertEquals(20, buffer.capacity());
            assertEquals(20, buffer.writableBytes());
            
            // 3. 데이터가 있을 때 용량 변경
            buffer.writeInt(100);
            buffer.capacity(30);
            assertEquals(100, buffer.getInt(0), "용량 변경 시 데이터는 유지됩니다");
            
            // 4. 최대 용량
            assertTrue(buffer.maxCapacity() >= 30, "maxCapacity는 현재 capacity 이상");
            
        } finally {
            buffer.release();
        }
    }
}
