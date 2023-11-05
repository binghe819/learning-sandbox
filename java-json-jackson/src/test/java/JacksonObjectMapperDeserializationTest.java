import com.binghe.domain.Member;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class JacksonObjectMapperDeserializationTest {

    @Test
    void json_to_object() throws JsonProcessingException {
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
    void json_array_to_objects() throws JsonProcessingException {
        // given
        ObjectMapper objectMapper = new ObjectMapper();

        String json = "[{\"id\":\"123\",\"name\":\"binghe\",\"age\":28},{\"id\":\"456\",\"name\":\"binghe2\",\"age\":29}]";

        // when
        Member[] members = objectMapper.readValue(json, Member[].class);

        // then
        assertThat(members.length).isEqualTo(2);
        assertThat(members[0].getId()).isEqualTo("123");
        assertThat(members[0].getName()).isEqualTo("binghe");
        assertThat(members[0].getAge()).isEqualTo(28);
        assertThat(members[1].getId()).isEqualTo("456");
        assertThat(members[1].getName()).isEqualTo("binghe2");
        assertThat(members[1].getAge()).isEqualTo(29);
    }

    @Test
    void json_list_to_objects() throws JsonProcessingException {
        // given
        ObjectMapper objectMapper = new ObjectMapper();

        String json = "[{\"id\":\"123\",\"name\":\"binghe\",\"age\":28},{\"id\":\"456\",\"name\":\"binghe2\",\"age\":29}]";

        // when
//        List<Member> members = objectMapper.readValue(json, List.class);
        List<Member> members = objectMapper.readValue(json, new TypeReference<List<Member>>() {});

        // then
        assertThat(members.size()).isEqualTo(2);
        assertThat(members.get(0).getId()).isEqualTo("123");
        assertThat(members.get(0).getName()).isEqualTo("binghe");
        assertThat(members.get(0).getAge()).isEqualTo(28);
        assertThat(members.get(1).getId()).isEqualTo("456");
        assertThat(members.get(1).getName()).isEqualTo("binghe2");
        assertThat(members.get(1).getAge()).isEqualTo(29);
    }

    @Test
    void json_to_map() throws JsonProcessingException {
        // given
        ObjectMapper objectMapper = new ObjectMapper();

        String json = "{\"id\":\"123\",\"name\":\"binghe\",\"age\":28}";

        // when
        Map<String, Object> jsonMap = objectMapper.readValue(json, new TypeReference<Map<String, Object>>(){});

        // then
        assertThat(jsonMap.get("id")).isEqualTo("123");
        assertThat(jsonMap.get("name")).isEqualTo("binghe");
        assertThat(jsonMap.get("age")).isEqualTo(28);
    }

    @Test
    void json_to_object_primitive_null() throws JsonProcessingException {
        // given
        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true);
        String json = "{\"id\":\"123\",\"name\":\"binghe\",\"age\":null}";

        // when
        Member member = objectMapper.readValue(json, Member.class);

        // then
        assertThat(member.getId()).isEqualTo("123");
        assertThat(member.getName()).isEqualTo("binghe");
        assertThat(member.getAge()).isEqualTo(0);
    }
}
