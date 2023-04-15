package ex03_serializable_inheritance;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

class Serializable_Inheritance_Test {

    /**
     * Serializable를 구현한 클래스의 상속받은 하위 클래스도 동일하게 Serializable을 구현하게 된다.
     *
     * 이때 주의할 점은 하위 클래스가 참조하는 객체도 모두 Serializable을 구현해야한다는 것이다.
     */
    @Test
    void whenSerializingAndDeserializingSubClass_thenCorrect() throws IOException, ClassNotFoundException {
        // given
        SoftwareDeveloper softwareDeveloper = new SoftwareDeveloper("binghe", 30,  "java");

        // when
        // serializing
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(softwareDeveloper); oos.flush(); oos.close();
        byte[] serializedSoftwareDeveloper = baos.toByteArray();
        System.out.println(Base64.getEncoder().encodeToString(serializedSoftwareDeveloper));

        // deserializing
        ByteArrayInputStream bais = new ByteArrayInputStream(serializedSoftwareDeveloper);
        ObjectInputStream ois = new ObjectInputStream(bais);
        Object objectMember = ois.readObject();
        SoftwareDeveloper deserializedMember = (SoftwareDeveloper) objectMember;

        // then
        assertThat(deserializedMember.getName()).isEqualTo(softwareDeveloper.getName());
        assertThat(deserializedMember.getAge()).isEqualTo(softwareDeveloper.getAge());
        assertThat(deserializedMember.getLanguage()).isEqualTo(softwareDeveloper.getLanguage());
    }

}