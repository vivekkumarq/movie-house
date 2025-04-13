package com.moviehouse.userservice.dataaccess.model;

import com.sun.istack.NotNull;
import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
@TypeDef(name = "json", typeClass = JsonType.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9]{4,20}$")
    @Column(unique = true)
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String name;
    @NotNull
    private String phoneNumber;
    @Type(type = "json")
    private Reference location;
}
