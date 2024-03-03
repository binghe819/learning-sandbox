package JavaToKt_2_null;

import org.jetbrains.annotations.Nullable;

public class PlatformType_Member {

    private final String name;

    public PlatformType_Member(String name) {
        this.name = name;
    }

//    @Nullable
    public String getName() {
        return name;
    }
}
