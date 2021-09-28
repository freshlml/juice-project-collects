package com.project.normal.test.lucence;

import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.search.join.*;
import org.apache.lucene.search.join.ScoreMode;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author mz    <br>
 * @CreateDate 2019/12/28   <br>
 * @Descrption TODO      <br>
 */
public class LuceneJoin {
    //lucene 7.7.0版本

    public List<Document> listDocs(){
        List<Document> docs = new ArrayList<>();
        Document child1_1 = new Document();
        child1_1.add(new StringField("id", "article1", Field.Store.YES));
        child1_1.add(new StringField("type", "article", Field.Store.YES));
        child1_1.add(new TextField("content", "青山握着眷恋", Field.Store.YES));
        docs.add(child1_1);

        Document child1_2 = new Document();
        child1_2.add(new StringField("id", "article2", Field.Store.YES));
        child1_2.add(new StringField("type", "article", Field.Store.YES));
        child1_2.add(new TextField("content", "青山握着眷恋,绿水拥尽缠绵", Field.Store.YES));
        docs.add(child1_2);

        Document child = new Document();
        child.add(new StringField("id", "user1", Field.Store.YES));
        child.add(new StringField("type", "user", Field.Store.YES));
        docs.add(child);

        Document child2_1 = new Document();
        child2_1.add(new StringField("id", "article3", Field.Store.YES));
        child2_1.add(new StringField("type", "article", Field.Store.YES));
        child2_1.add(new TextField("content", "青山握着眷恋,绿水拥尽缠绵,世上多少爱与恨", Field.Store.YES));
        docs.add(child2_1);

        Document childP = new Document();
        childP.add(new StringField("id", "user2", Field.Store.YES));
        childP.add(new StringField("type", "user", Field.Store.YES));
        docs.add(childP);

        return docs;
    }

    private Path getPath1() {
        return Paths.get("D:\\index\\down2");
    }


    public void gown() throws Exception {
        List<Document> lists = listDocs();
        //创建存储对象
        FSDirectory directory = FSDirectory.open(getPath1());
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

    //index time join
    public void nup() throws Exception {
        FSDirectory directory = FSDirectory.open(getPath1());
        DirectoryReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);

        BitSetProducer parentFilter = new QueryBitSetProducer(new TermQuery(new Term("type", "user")));
        TermQuery childQuery = new TermQuery(new Term("content", "世上"));

        ToParentBlockJoinQuery toParentBlockJoinQuery = new ToParentBlockJoinQuery(childQuery, parentFilter, ScoreMode.None);

        TopDocs results = searcher.search(toParentBlockJoinQuery, 10);

        for (int i = 0; i < results.scoreDocs.length; i++) {
            ScoreDoc scoreDoc = results.scoreDocs[i];
            Document document = searcher.doc(scoreDoc.doc);
            System.out.println(document);

            ParentChildrenBlockJoinQuery parentChildrenBlockJoinQuery =
                    new ParentChildrenBlockJoinQuery(parentFilter, childQuery, scoreDoc.doc);

            TopDocs topChildResults = searcher.search(parentChildrenBlockJoinQuery, 3);

            for (int j = 0; j < topChildResults.scoreDocs.length; j++) {
                System.out.println(searcher.doc(topChildResults.scoreDocs[j].doc));
            }
        }
    }


    public List<Document> listDocs2(){
        List<Document> docs = new ArrayList<>();

        Document one = new Document();
        one.add(new StringField("id", "1", Field.Store.YES));
        one.add(new SortedDocValuesField("id", new BytesRef("1")));
        one.add(new TextField("name", "中文", Field.Store.YES));
        docs.add(one);

        Document one1 = new Document();
        one1.add(new StringField("id", "2", Field.Store.YES));
        one1.add(new SortedDocValuesField("id", new BytesRef("2")));
        one1.add(new StringField("fid", "1", Field.Store.YES));
        one1.add(new SortedDocValuesField("fid", new BytesRef("1")));
        one1.add(new TextField("price", "10", Field.Store.YES));
        docs.add(one1);

        Document one2 = new Document();
        one2.add(new StringField("id", "3", Field.Store.YES));
        one2.add(new SortedDocValuesField("id", new BytesRef("3")));
        one2.add(new StringField("fid", "1", Field.Store.YES));
        one2.add(new SortedDocValuesField("fid", new BytesRef("1")));
        one2.add(new TextField("price", "10.1", Field.Store.YES));
        docs.add(one2);

        Document two = new Document();
        two.add(new StringField("id", "5", Field.Store.YES));
        two.add(new SortedDocValuesField("id", new BytesRef("5")));
        two.add(new TextField("name", "世界", Field.Store.YES));
        docs.add(two);

        Document two1 = new Document();
        two1.add(new StringField("id", "6", Field.Store.YES));
        two1.add(new SortedDocValuesField("id", new BytesRef("6")));
        two1.add(new StringField("fid", "5", Field.Store.YES));
        two1.add(new SortedDocValuesField("fid", new BytesRef("5")));
        two1.add(new TextField("desc", "青山绿水", Field.Store.YES));
        docs.add(two1);

        return docs;
    }

    private Path getPath2() {
        return Paths.get("D:\\index\\down3");
    }

    public void mdown() throws Exception {
        List<Document> lists = listDocs2();
        //创建存储对象
        FSDirectory directory = FSDirectory.open(getPath2());
        //创建分词器
        SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();
        //创建写入器配置对象
        IndexWriterConfig writerConfig = new IndexWriterConfig(analyzer);
        writerConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        //创建写入器对象
        IndexWriter writer = new IndexWriter(directory, writerConfig);
        //写入文档
        writer.addDocuments(lists);
        //提交
        writer.commit();
        //关闭
        writer.close();
    }

    //query one join
    public void mup() throws Exception {

        FSDirectory directory = FSDirectory.open(getPath2());
        DirectoryReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);

        //1-n: 先查询1(fromField)，在查询n(toField)
        Query query = JoinUtil.createJoinQuery("id", false, "fid",
                    new TermQuery(new Term("name", "世界")), searcher, ScoreMode.None);

        TopDocs results = searcher.search(query, 100);
        System.out.println(results.totalHits);
        ScoreDoc[] scoreDocs = results.scoreDocs;
        for(ScoreDoc scoreDoc : scoreDocs) {
            Document doc = searcher.doc(scoreDoc.doc);
            System.out.println(doc);
        }
        //上述查询 scoreDocs中只包括n方数据,并且没办法对n方过滤

        System.out.println("#################");
        //n-1: 先查询n(fromField)， 在查询1(toField)
        Query query1 = JoinUtil.createJoinQuery("fid", false, "id",
                new TermQuery(new Term("desc", "青山绿水")), searcher, ScoreMode.None);

        TopDocs results1 = searcher.search(query1, 100);
        System.out.println(results1.totalHits);
        ScoreDoc[] scoreDocs1 = results1.scoreDocs;
        for(ScoreDoc scoreDoc : scoreDocs1) {
            Document doc = searcher.doc(scoreDoc.doc);
            System.out.println(doc);
        }
        //上述查询 scoreDocs只包含1方数据

    }

}

