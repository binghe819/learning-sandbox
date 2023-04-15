package ex05_serialization_metainfo;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class Address implements Serializable {

    private String address;

    public Address(String address) {
        this.address = address;
    }
}
