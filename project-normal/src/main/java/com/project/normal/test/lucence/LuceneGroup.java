package com.project.normal.test.lucence;

import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.search.grouping.BlockGroupingCollector;
import org.apache.lucene.search.grouping.GroupDocs;
import org.apache.lucene.search.grouping.GroupingSearch;
import org.apache.lucene.search.grouping.TopGroups;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class LuceneGroup {
    //lucene 7.7.0

    public List<Document> listDocs(){
        List<Document> docs = new ArrayList<>();

        Document one1 = new Document();
        one1.add(new StringField("id", "2", Field.Store.YES));
        one1.add(new SortedDocValuesField("id", new BytesRef("2")));
        one1.add(new StringField("fid", "1", Field.Store.YES));
        one1.add(new SortedDocValuesField("fid", new BytesRef("1")));
        one1.add(new TextField("price", "10", Field.Store.YES));
        one1.add(new NumericDocValuesField("rq", 123L));
        docs.add(one1);

        Document one2 = new Document();
        one2.add(new StringField("id", "3", Field.Store.YES));
        one2.add(new SortedDocValuesField("id", new BytesRef("3")));
        one2.add(new StringField("fid", "1", Field.Store.YES));
        one2.add(new SortedDocValuesField("fid", new BytesRef("1")));
        one2.add(new TextField("price", "10", Field.Store.YES));
        one2.add(new NumericDocValuesField("rq", 113L));
        docs.add(one2);

        Document one = new Document();
        one.add(new StringField("id", "1", Field.Store.YES));
        one.add(new SortedDocValuesField("id", new BytesRef("1")));
        one.add(new TextField("name", "中文", Field.Store.YES));
        one.add(new StringField("type", "user", Field.Store.YES));
        one.add(new NumericDocValuesField("rq", 111L));
        docs.add(one);


        Document two1 = new Document();
        two1.add(new StringField("id", "6", Field.Store.YES));
        two1.add(new SortedDocValuesField("id", new BytesRef("6")));
        two1.add(new StringField("fid", "5", Field.Store.YES));
        two1.add(new SortedDocValuesField("fid", new BytesRef("5")));
        two1.add(new TextField("desc", "青山绿水", Field.Store.YES));
        two1.add(new TextField("price", "10", Field.Store.YES));
        two1.add(new NumericDocValuesField("rq", 115L));
        docs.add(two1);

        Document two = new Document();
        two.add(new StringField("id", "5", Field.Store.YES));
        two.add(new SortedDocValuesField("id", new BytesRef("5")));
        two.add(new TextField("name", "世界", Field.Store.YES));
        two.add(new StringField("type", "user", Field.Store.YES));
        two.add(new NumericDocValuesField("rq", 222L));
        docs.add(two);

        return docs;
    }

    private Path getPath() {
        return Paths.get("D:\\index\\down4");
    }


    public void gown() throws Exception {
        List<Document> lists = listDocs();
        //创建存储对象
        FSDirectory directory = FSDirectory.open(getPath());
        //创建分词器
        SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();
        //创建写入器配置对象
        IndexWriterConfig writerConfig = new IndexWriterConfig(analyzer);
        writerConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        //创建写入器对象
        IndexWriter writer = new IndexWriter(directory, writerConfig);
        //写入文档
        //writer.addDocument(document);
        writer.addDocuments(lists);
        //提交
        writer.commit();
        //关闭
        writer.close();
    }


    public void kup() throws Exception {
        //index time and BlockGroupingCollector
        //index time join中设定的文档结构为 childDoc1-childDoc2-parentDoc
        //使用BlockGroupingCollector对结果进行收集

        FSDirectory directory = FSDirectory.open(getPath());
        DirectoryReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);

        TermQuery parentQuery = new TermQuery(new Term("type", "user"));
        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        builder.add(parentQuery, BooleanClause.Occur.FILTER);
        BooleanQuery parentFilter = builder.build();
        //Filter groupEndDocs = new CachingWrapperFilter(new QueryWrapperFilter(new TermQuery(new Term("groupEnd", "x"))));

        //TermQuery parentQuery = new TermQuery(new Term("type", "user"));
        //BitSetProducer parentFilter = new QueryBitSetProducer(parentQuery);

        TermQuery childQuery = new TermQuery(new Term("content", "青山"));

        BlockGroupingCollector c = new BlockGroupingCollector(new Sort(new SortField("id", SortField.Type.STRING, false)),
                10000, false, parentFilter.createWeight(searcher, false, 1));

        searcher.search(childQuery, c);

        TopGroups<?> results = c.getTopGroups(new Sort(new SortField("rq", SortField.Type.LONG, false)),
                0, 0, 10000, true);

        System.out.println(results.totalHitCount);
        System.out.println(results.totalGroupCount);
        System.out.println(results.totalGroupedHitCount);
        for (GroupDocs group : results.groups) {
            System.out.println(group.totalHits);
        }
    }


    public void jup() throws Exception {
        //two-pass grouping
        // [groupQuery groupField]
        // [groupSort groupOffset groupLimit]
        // [groupDocsSort groupDocsOffset groupDocsLimit]

        FSDirectory directory = FSDirectory.open(getPath());
        DirectoryReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);

        GroupingSearch groupingSearch = new GroupingSearch("fid");

        groupingSearch.setGroupSort(new Sort(new SortField("id", SortField.Type.STRING, false)));
        groupingSearch.setSortWithinGroup(new Sort(new SortField("rq", SortField.Type.LONG, false)));
        groupingSearch.setFillSortFields(true);
        groupingSearch.setAllGroups(true);  //?
        groupingSearch.setAllGroupHeads(true); //?
        groupingSearch.setGroupDocsOffset(0);
        groupingSearch.setGroupDocsLimit(10000);  //一般而言 组内offset和limit分页

        TermQuery query = new TermQuery(new Term("price", "10"));
        TopGroups<BytesRef> results = groupingSearch.search(searcher, query, 0, 10000); //一般而言 组间offset和limit分页

        System.out.println(results.totalHitCount);
        System.out.println(results.totalGroupCount);

        for (GroupDocs<BytesRef> group : results.groups) {
            System.out.println("当前组: " + group.groupValue.utf8ToString() + " 组内数量: " + group.totalHits);
            for(ScoreDoc scoreDoc : group.scoreDocs) {
                Document doc = searcher.doc(scoreDoc.doc);
                System.out.println(doc);
            }
        }
        //https://www.cnblogs.com/davidwang456/articles/11913868.html
    }

}
