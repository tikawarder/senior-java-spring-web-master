package hu.ponte.hr.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

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
	private byte[] imageData;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ImageMeta imageMeta = (ImageMeta) o;
		return size == imageMeta.size && Objects.equals(id, imageMeta.id) && Objects.equals(name, imageMeta.name) && Objects.equals(mimeType, imageMeta.mimeType) && Objects.equals(digitalSign, imageMeta.digitalSign) && Arrays.equals(imageData, imageMeta.imageData);
	}

	@Override
	public int hashCode() {
		int result = Objects.hash(id, name, mimeType, size, digitalSign);
		result = 31 * result + Arrays.hashCode(imageData);
		return result;
	}
}
