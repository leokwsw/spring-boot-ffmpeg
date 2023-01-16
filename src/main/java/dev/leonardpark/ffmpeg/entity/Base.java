package dev.leonardpark.ffmpeg.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Base implements Serializable {
  @Id
  private String id;
  @CreatedDate
  private Date createdAt = new Date();
  @LastModifiedDate
  private Date updateAt;
  private boolean dbDelete = false;
}
