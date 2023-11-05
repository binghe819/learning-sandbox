import com.binghe.domain.Member;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class JacksonObjectMapperSerializationTest {

    @Test
    void object_to_json_FileOutputStream() throws IOException {
        // given
        ObjectMapper objectMapper = new ObjectMapper();

        // when
        Member member = Member.builder()
                .id("123")
                .name("binghe")
                .age(28)
                .build();

        objectMapper.writeValue(new FileOutputStream("data/member.json"), member);
    }

    @Test
    void object_to_json_String() throws JsonProcessingException {
        // given
        ObjectMapper objectMapper = new ObjectMapper();

        // when
        Member member = Member.builder()
                .id("123")
                .name("binghe")
                .age(28)
                .build();
        String json = objectMapper.writeValueAsString(member);

        // then
        assertThat(json).isEqualTo("{\"id\":\"123\",\"name\":\"binghe\",\"age\":28}");
    }

    @Test
    void object_to_json_custom_serializer() throws JsonProcessingException {
        // given
        ObjectMapper objectMapper = new ObjectMapper();
        MemberCustomSerializer memberCustomSerializer = new MemberCustomSerializer();

        SimpleModule module = new SimpleModule("MemberCustomSerializer");
        module.addSerializer(Member.class, memberCustomSerializer);

        objectMapper.registerModule(module);

        // when
        Member mem = Member.builder()
                .id("123")
                .name("binghe")
                .age(28)
                .build();
        String json = objectMapper.writeValueAsString(mem);

        // then
        assertThat(json).isEqualTo("{\"memberId\":\"123\",\"nickName\":\"binghe\",\"age\":28}");
    }

    private static class MemberCustomSerializer extends JsonSerializer<Member> {

        @Override
        public void serialize(Member value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            gen.writeStartObject(); // {

            gen.writeStringField("memberId", value.getId());
            gen.writeStringField("nickName", value.getName());
            gen.writeNumberField("age", value.getAge());

            gen.writeEndObject(); // }
        }
    }
}
