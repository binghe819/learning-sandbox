package ex01_simple_serialization_deserialization;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class Simple_Serialization_Deserialization_Test {

    /**
     * <직렬화>
     * - static, transient 값은 직렬화 되지 않는다. (직렬화 대상이 아님)
     * - Serializable을 구현하고있지 않으면 직렬화가 되지 않는다. (java.io.NotSerializableException: ex04_serialization_versionUID.Person 발생)
     *
     * <역직렬화>
     * - transient 값은 null로 초기화된다.
     */
    @Test
    void whenSerializingAndDeserializing_thenCorrect() throws IOException, ClassNotFoundException {
        // given
        Person person = new Person("binghe", 30, "설명");

        // when
        // serializing
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(person); oos.flush(); oos.close();
        byte[] serializedPerson = baos.toByteArray();
        System.out.println(Base64.getEncoder().encodeToString(serializedPerson));

        // deserializing
        ByteArrayInputStream bais = new ByteArrayInputStream(serializedPerson);
        ObjectInputStream ois = new ObjectInputStream(bais);
        Object objectMember = ois.readObject();
        Person deserializedPerson = (Person) objectMember;

        // then
        assertThat(deserializedPerson.getName()).isEqualTo(person.getName());
        assertThat(deserializedPerson.getAge()).isEqualTo(person.getAge());

        // transient는 역직렬화 되지 않는다. (역직렬화하면 null로 초기화된다.)
        assertThat(deserializedPerson.getDescription()).isNull();
        assertThat(deserializedPerson.EMPTY).isEqualTo(person.EMPTY);
    }

}