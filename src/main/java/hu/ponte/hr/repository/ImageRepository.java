package hu.ponte.hr.repository;

import hu.ponte.hr.controller.ImageMeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.*;
import java.util.Optional;
@Repository
public interface ImageRepository extends JpaRepository<ImageMeta, String> {
    Optional<ImageMeta> findById(String id);
}

