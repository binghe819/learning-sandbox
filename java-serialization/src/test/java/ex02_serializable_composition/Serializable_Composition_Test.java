package ex02_serializable_composition;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class Serializable_Composition_Test {

    /**
     * Serializable을 구현하고있는 객체를 직렬화/역직렬화하기위해선, 해당 객체가 참조하는 모든 객체도 Serializable을 구현해야한다.
     *
     * 그렇지 않으면, NotSerializableException이 발생한다.
     *
     */
    @Test
    void whenSerializing_thenThrowNotSerializableException() throws IOException {
        // given
        Address address = new Address("서울"); // not implement serializable object
        Person person = new Person("binghe", 30, "설명", address);

        // when, then
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        assertThatThrownBy(() -> oos.writeObject(person))
                .isInstanceOf(NotSerializableException.class);
    }

    /**
     * Address에 Serializable 구현하고 실행하면 성공함.
     */
    @Disabled
    @Test
    void whenSerializingAndDeserializing_thenThrowNotSerializableException() throws IOException, ClassNotFoundException {
        // given
        Address address = new Address("서울");
        Person person = new Person("binghe", 30, "설명", address);

        // when
        // serializing
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(person); oos.flush(); oos.close();
        byte[] serializedMember = baos.toByteArray();
        System.out.println(Base64.getEncoder().encodeToString(serializedMember));

        // deserializing
        ByteArrayInputStream bais = new ByteArrayInputStream(serializedMember);
        ObjectInputStream ois = new ObjectInputStream(bais);
        Object objectPerson = ois.readObject();
        Person deserializedPerson = (Person) objectPerson;

        // then
        assertThat(deserializedPerson.getName()).isEqualTo(person.getName());
        assertThat(deserializedPerson.getAge()).isEqualTo(person.getAge());
        assertThat(deserializedPerson.getDescription()).isNull();
        assertThat(deserializedPerson.getAddress()).isEqualTo(person.getAddress());
    }
}