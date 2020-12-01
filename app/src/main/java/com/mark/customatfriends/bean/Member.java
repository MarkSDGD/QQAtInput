package com.mark.customatfriends.bean;

/**
 * Created by Mark on 2020/11/26.
 * <p>Copyright 2020 ZTZQ.</p>
 */
public class Member {

    String name;
    String id;

    public Member(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
