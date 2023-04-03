package hu.ponte.hr.controller;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;

import javax.persistence.*;

/**
 * @author zoltan
 */
@Entity
@Table(name = "image")
@Getter
@Builder
public class ImageMeta {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;
	private String name;
	private String mimeType;
	private long size;
	private String digitalSign;
	@Lob
	@Column(name = "imageData", length = 2000)
	private byte[] imageData;
}
