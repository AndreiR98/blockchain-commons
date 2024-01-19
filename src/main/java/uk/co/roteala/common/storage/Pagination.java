package uk.co.roteala.common.storage;

import lombok.Data;
import uk.co.roteala.common.BasicModel;

import java.util.List;

@Data
public class Pagination {
    private List<Page> pages;

    public Pagination () {}

    public void createPage() {
        //this.pages.add();
    }
}
