namespace java com.binghe.thrift

struct SmallObject {
    1: i64 id;
    2: string name;
    3: bool age;
}

struct LargeObject {
    1: i64 id;
    2: string name;
    3: list<SmallObject> smallObjects;
}
