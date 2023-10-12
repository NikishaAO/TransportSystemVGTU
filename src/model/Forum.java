package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.stream.events.Comment;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Forum {
    private int id;
    private String title;
    private List<Comment> commentList;

    public Forum(String title) {
        this.title = title;
    }

    public Forum(int id, String title) {
        this.id = id;
        this.title = title;
    }
}
