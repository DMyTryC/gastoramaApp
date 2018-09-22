package com.uga.gastorama.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ConfectionerPhoto.
 */
@Entity
@Table(name = "confectioner_photo")
@Document(indexName = "confectionerphoto")
public class ConfectionerPhoto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "url", nullable = false)
    private String url;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("photos")
    private Confectioner confectioner;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public ConfectionerPhoto url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Confectioner getConfectioner() {
        return confectioner;
    }

    public ConfectionerPhoto confectioner(Confectioner confectioner) {
        this.confectioner = confectioner;
        return this;
    }

    public void setConfectioner(Confectioner confectioner) {
        this.confectioner = confectioner;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConfectionerPhoto confectionerPhoto = (ConfectionerPhoto) o;
        if (confectionerPhoto.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), confectionerPhoto.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ConfectionerPhoto{" +
            "id=" + getId() +
            ", url='" + getUrl() + "'" +
            "}";
    }
}
