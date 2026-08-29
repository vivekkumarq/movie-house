package com.moviehouse.userservice.dataaccess.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
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

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9]{4,20}$", message = "must be 4 to 20 alphanumeric characters")
    @Column(unique = true, nullable = false)
    private String username;

    @NotBlank
    @Size(min = 8, max = 72, message = "must be at least 8 characters")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String password;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Pattern(regexp = "^[0-9]{10}$", message = "must be a 10 digit number")
    @Column(nullable = false)
    private String phoneNumber;

    @NotNull
    @Valid
    @Type(type = "json")
    private Reference location;
}
