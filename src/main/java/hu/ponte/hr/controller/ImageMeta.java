package hu.ponte.hr.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author zoltan
 */
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageMeta {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;
	private String name;
	private String mimeType;
	private long size;
	@Lob
	private String digitalSign;
	@Lob
	@Column(name = "imageData", length = 2000)
	private byte[] imageData;
}
