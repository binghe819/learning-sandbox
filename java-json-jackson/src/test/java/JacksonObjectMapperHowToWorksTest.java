import com.binghe.domain.Member;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.io.SegmentedStringWriter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class JacksonObjectMapperHowToWorksTest {

    @Test
    void objectMapper_serialization() throws JsonProcessingException {
        // given
        ObjectMapper objectMapper = new ObjectMapper();
        Member member = Member.builder()
                .id("123")
                .name("binghe")
                .age(28)
                .build();

        // when
        String serializedJson = objectMapper.writeValueAsString(member);

        // then
        assertThat(serializedJson).isEqualTo("{\"id\":\"123\",\"name\":\"binghe\",\"age\":28}");
    }

    @Test
    void objectMapper_serialization_debug() throws IOException {
        // given
        Member member = Member.builder()
                .id("123")
                .name("binghe")
                .age(28)
                .build();

        // when
        //  The main factory class of Jackson package, used to configure and construct reader (aka parser, JsonParser) and writer (aka generator, JsonGenerator) instances.
        JsonFactory jsonFactory = new JsonFactory();
        // Base class that defines public API for writing JSON content. Instances are created using factory methods of a JsonFactory instance.
        SegmentedStringWriter segmentedStringWriter = new SegmentedStringWriter(jsonFactory._getBufferRecycler());
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(segmentedStringWriter);

        jsonGenerator.writeStartObject(); // '{' 생성 코드.
        jsonGenerator.writeStringField("id", member.getId());
        jsonGenerator.writeStringField("name", member.getName());
        jsonGenerator.writeNumberField("age", member.getAge());
        jsonGenerator.writeEndObject(); // '}' 생성 코드.

        jsonGenerator.close();

        // thenn
        assertThat(segmentedStringWriter.getAndClear()).isEqualTo("{\"id\":\"123\",\"name\":\"binghe\",\"age\":28}");
    }

    @Test
    void objectMapper_deserialization() throws JsonProcessingException {
        // given
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "{\"id\":\"123\",\"name\":\"binghe\",\"age\":28}";

        // when
        Member member = objectMapper.readValue(json, Member.class);

        // then
        assertThat(member.getId()).isEqualTo("123");
        assertThat(member.getName()).isEqualTo("binghe");
        assertThat(member.getAge()).isEqualTo(28);
    }

    @Test
    void objectMapper_deserialization_debug() throws IOException {
        // given
        String json = "{\"id\":\"123\",\"name\":\"binghe\",\"age\":28}";

        // when
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser parser = jsonFactory.createParser(json);

        Member member = new Member();
        while(!parser.isClosed()){
            JsonToken jsonToken = parser.nextToken();

            // token이 field 이름이면
            if (JsonToken.FIELD_NAME.equals(jsonToken)) {
                String fieldName = parser.getCurrentName();
                System.out.print("fieldName: " + fieldName);

                // field의 value
                jsonToken = parser.nextToken();
                System.out.println(", value: " + parser.getValueAsString());

                if ("id".equals(fieldName)) {
                    member.setId(parser.getValueAsString());
                } else if ("name".equals(fieldName)) {
                    member.setName(parser.getValueAsString());
                } else if ("age".equals(fieldName)) {
                    member.setAge(parser.getValueAsInt());
                }
            }
        }

        // then
        assertThat(member.getId()).isEqualTo("123");
        assertThat(member.getName()).isEqualTo("binghe");
        assertThat(member.getAge()).isEqualTo(28);
    }

    @Test
    void objectMapper_deserialization_readTree() throws JsonProcessingException {
        // given
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "{\"id\":\"123\",\"name\":\"binghe\",\"age\":28}";

        // when
        JsonNode jsonNode = objectMapper.readTree(json);
        Member member = objectMapper.treeToValue(jsonNode, Member.class);

        // then
        assertThat(member.getId()).isEqualTo("123");
        assertThat(member.getName()).isEqualTo("binghe");
        assertThat(member.getAge()).isEqualTo(28);
    }
}
