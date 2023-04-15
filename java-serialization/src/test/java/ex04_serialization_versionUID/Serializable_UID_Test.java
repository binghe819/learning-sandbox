package ex04_serialization_versionUID;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class Serializable_UID_Test {

    @Test
    void whenDeserializingChangedObject_thenThrowInvalidClassException() throws IOException, ClassNotFoundException {
        // given (name, age만 가진 ex04_serialization_versionUID.Person 객체를 직렬화한 base64 데이터)
        String deserializedPersonBase64 = "rO0ABXNyACRleDA0X3NlcmlhbGl6YXRpb25fdmVyc2lvblVJRC5QZXJzb26/7herOgpoOAIAAkkAA2FnZUwABG5hbWV0ABJMamF2YS9sYW5nL1N0cmluZzt4cAAAAB50AAZiaW5naGU=";
        byte[] deserializedPersonBase = Base64.getDecoder().decode(deserializedPersonBase64);

        // when
        ByteArrayInputStream bais = new ByteArrayInputStream(deserializedPersonBase);
        ObjectInputStream ois = new ObjectInputStream(bais);

        // then
        assertThatThrownBy(() -> ois.readObject())
            .isInstanceOf(InvalidClassException.class);
//        ois.readObject();
    }

    /**
     * ex04_serialization_versionUID.Person 직렬화하기 위한 유틸성 테스트.
     */
    @Test
    void serializingPerson() throws IOException {
        // given
        Person person = new Person("binghe", 30);

        // when, then
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(person); oos.flush(); oos.close();
        byte[] serializedPerson = baos.toByteArray();
        System.out.println(Base64.getEncoder().encodeToString(serializedPerson));
    }
}