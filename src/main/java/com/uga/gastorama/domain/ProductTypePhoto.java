package com.uga.gastorama.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ProductTypePhoto.
 */
@Entity
@Table(name = "product_type_photo")
@Document(indexName = "producttypephoto")
public class ProductTypePhoto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "url", nullable = false)
    private String url;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("photos")
    private ProductType productType;

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

    public ProductTypePhoto url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ProductType getProductType() {
        return productType;
    }

    public ProductTypePhoto productType(ProductType productType) {
        this.productType = productType;
        return this;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
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
        ProductTypePhoto productTypePhoto = (ProductTypePhoto) o;
        if (productTypePhoto.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), productTypePhoto.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProductTypePhoto{" +
            "id=" + getId() +
            ", url='" + getUrl() + "'" +
            "}";
    }
}
