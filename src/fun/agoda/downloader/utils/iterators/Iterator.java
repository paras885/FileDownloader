package fun.agoda.downloader.utils.iterators;

public interface Iterator<IteratorType> {

    boolean hasNext();

    IteratorType next();
}
