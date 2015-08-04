package com.boivin.model;

/**
 * Created by peterboivin on 8/2/15.
 */

import com.google.api.services.plus.Plus;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class People {
    @Id
    Long id;
    Plus.People people;

    private People() {}

    public People(Plus.People people) {
        this.people = people;
    }
}
