package ex05_serialization_metainfo;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class Serializable_MetaInfo_Test {

    @Test
    void whenChangeMetaInfo_thenThrowException() throws IOException {
        // given (Address 패키지의 주소가 변경되기 전)
        String deserializablePersonBase64 = "rO0ABXNyACJleDA1X3NlcmlhbGl6YXRpb25fbWV0YWluZm8uUGVyc29uAAAAAAAAAAECAANJAANhZ2VMAAdhZGRyZXNzdAAlTGV4MDVfc2VyaWFsaXphdGlvbl9tZXRhaW5mby9BZGRyZXNzO0wABG5hbWV0ABJMamF2YS9sYW5nL1N0cmluZzt4cAAAAB5zcgAjZXgwNV9zZXJpYWxpemF0aW9uX21ldGFpbmZvLkFkZHJlc3NKdMDMl8wkDAIAAUwAB2FkZHJlc3NxAH4AAnhwdAAFc2VvdWx0AAZiaW5naGU=";
        byte[] deserializedPersonBase = Base64.getDecoder().decode(deserializablePersonBase64);

        // when
        ByteArrayInputStream bais = new ByteArrayInputStream(deserializedPersonBase);
        ObjectInputStream ois = new ObjectInputStream(bais);

        // then
        assertThatThrownBy(() -> ois.readObject())
                .isInstanceOf(ClassNotFoundException.class);
    }


    /**
     * Person 직렬화하기 위한 유틸성 테스트.
     */
    @Test
    void serializingPerson() throws IOException {
        // given
        Address address = new Address("seoul");
        Person person = new Person("binghe", 30, address);

        // when, then
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(person); oos.flush(); oos.close();
        byte[] serializedPerson = baos.toByteArray();
        System.out.println(Base64.getEncoder().encodeToString(serializedPerson));
    }
}