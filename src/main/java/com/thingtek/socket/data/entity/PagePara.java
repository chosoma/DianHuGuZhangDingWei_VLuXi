package com.thingtek.socket.data.entity;

public class PagePara {

    private int startpage = 0;
    private int thispage;
    private int endpage;
    private int pageCount = 50;
    //    private int pagecount;
    //    private int count;

    public PagePara() {
        this.thispage = this.startpage;
    }

    public int getThispage() {
        return thispage + 1;
    }

    public void setCount(int count) {
//        this.count = count;
        if (count % pageCount == 0) {
            this.endpage = count / pageCount - 1;
            if (this.endpage <= 0) {
                endpage = 0;
            }
//            this.pagecount = count / pageCount;
        } else {
            this.endpage = count / pageCount;
//            this.pagecount = count / pageCount + 1;
        }
    }

    public void toPage(int page) {
        if (page - 1 >= endpage) {
            this.thispage = endpage;
            return;
        }
        this.thispage = page - 1;
    }

    public void toNextPage() {
        if (isEndPage()) {
            return;
        }
        if (this.thispage < endpage) {
            this.thispage++;
        }
    }

    public void toBeforPage() {
        if (isStartPage()) {
            return;
        }
        if (this.thispage > startpage) {
            this.thispage--;
        }
    }

    public void toStartPage() {
        if (isStartPage()) {
            return;
        }
        this.thispage = this.startpage;
    }

    public void toEndPage() {
        if (isEndPage()) {
            return;
        }
        this.thispage = this.endpage;
    }

    public int getEndpage() {
        return endpage + 1;
    }

    public boolean isStartPage() {
        return this.startpage == this.thispage;
    }

    public boolean isEndPage() {
        return this.endpage == this.thispage;
    }

    public int getStart() {
        return thispage * pageCount;
    }

    public int getPageCount() {
        return pageCount;
    }
}
