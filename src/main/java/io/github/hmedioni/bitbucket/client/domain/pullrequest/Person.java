package io.github.hmedioni.bitbucket.client.domain.pullrequest;


import org.springframework.lang.*;


public class Person {

    public User user;

    @Nullable
    public String role;

    public boolean approved;

    @Nullable
    public String status;

}
