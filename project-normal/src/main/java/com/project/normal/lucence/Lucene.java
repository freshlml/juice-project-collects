package com.project.normal.lucence;

import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author mz    <br>
 * @CreateDate 2019/12/28   <br>
 * @Descrption TODO      <br>
 */
public class Lucene {
    //lucene 7.7.0版本

    public List<Document> listDocs() {
        List<Document> docs = new ArrayList<>();
        Document doc1 = new Document();
        doc1.add(new StringField("id", "123456789", Field.Store.YES));  //一般的，数据库中使用的long型id这里应该使用字符串形式保存
        doc1.add(new TextField("text", "中华国", Field.Store.YES));
        doc1.add(new TextField("content", "青山握着眷恋", Field.Store.YES));
        doc1.add(new LongPoint("po", 1000000L));                        //long
        doc1.add(new TextField("rq", "1990-09-09 12:23:24", Field.Store.YES));  //日期转化成 日期格式串存储
        doc1.add(new LongPoint("rqq", 1234543434343L)); //时间戳Long型
        NumericDocValuesField n1 = new NumericDocValuesField("rqq", 1234543434343L);
        doc1.add(n1);
        docs.add(doc1);

        Document doc2 = new Document();
        doc2.add(new StringField("id", "234", Field.Store.YES));
        doc2.add(new TextField("text", "国大鱼", Field.Store.YES));
        doc2.add(new LongPoint("rqq", 1234543434341L));
        NumericDocValuesField n2 = new NumericDocValuesField("rqq", 1234543434341L);
        doc2.add(n2);
        TextField c2 = new TextField("content", "青山握着眷恋,绿水拥尽缠绵", Field.Store.YES);

        doc2.add(c2);

        docs.add(doc2);
        return docs;
    }
    private Path getPath() {
        return Paths.get("D:\\index\\down");
    }

    public void down() throws Exception {
        //创建文档对象
        /*Document document = new Document();
        document.add(new StringField("id", "123456789", Field.Store.YES));
        document.add(new TextField("content", "青山握着眷恋, 拨动滚滚红尘", Field.Store.YES));*/
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

    public void vup() throws Exception {
        //创建存储对象
        FSDirectory directory = FSDirectory.open(getPath());
        //创建存储对象读取器
        DirectoryReader reader = DirectoryReader.open(directory);
        //创建搜索器
        IndexSearcher searcher = new IndexSearcher(reader);
        //创建查询构造器
        SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();
        QueryParser qp = new QueryParser("text", analyzer);
        //Query parser = qp.parse("中");
        //Query parser = qp.parse("(中 OR 大^100) AND 国");
        //创建查询器
        Term t = new Term("content", "青山");
        Query query = new TermQuery(t);

        Query query1 = LongPoint.newExactQuery("po", 1000000L);  //实际上创建的  PointRangeQuery

        //查询
        TopDocs results = searcher.search(query, 10, new Sort(new SortField("rqq", SortField.Type.LONG, false)));
        System.out.println(results.totalHits);
        ScoreDoc[] scoreDocs = results.scoreDocs;
        for(ScoreDoc scoreDoc : scoreDocs) {
            Document doc = searcher.doc(scoreDoc.doc);
            System.out.println(doc);
        }
    }

    public void bup() throws Exception {
        FSDirectory directory = FSDirectory.open(getPath());
        DirectoryReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);

        //BooleanQuery
        TermQuery text = new TermQuery(new Term("text", "123"));
        TermQuery content = new TermQuery(new Term("content", "青山"));
        TermQuery content2 = new TermQuery(new Term("content", "绿水"));

        BooleanQuery.Builder builder1 = new BooleanQuery.Builder();
        builder1.add(content, BooleanClause.Occur.MUST);

        BooleanQuery.Builder builder2 = new BooleanQuery.Builder();
        builder2.add(text, BooleanClause.Occur.SHOULD);
        builder2.add(content2, BooleanClause.Occur.SHOULD);
        BooleanQuery query2 = builder2.build();
        builder1.add(query2, BooleanClause.Occur.MUST);

        BooleanQuery query1  = builder1.build();

        //BooleanQuery rules
        //ALL MUST 逻辑与          (where a=1 and b=2)
        //MUST SHOULD组合 SHOULD无效; MUST_NOT SHOULD MUST组合 SHOULD无效;
        //MUST_NOT MUST 组合; MUST_NOT SHOULD组合==>MUST_NOT MUST 组合   (where a=1 and b!=2)
        //SHOULD SHOULD 逻辑与     (where a=1 or b=2)
        //   where a!=1 and b!=2 能构造吗？？ === !(a==1 or b==2)
        //
        //错例展示
        //where a=1 and b=2 or c=3   对应   a-MUST b-MUST c-SHOULD   实际的结果: a-MUST b-MUST
        //
        //组合条件例子
        //where a=1 and (b=2 or c=3) 对应   a-MUST (b-SHOULD c-SHOULD) MUST


        TopDocs results = searcher.search(query1, 10);
        System.out.println(results.totalHits);
        ScoreDoc[] scoreDocs = results.scoreDocs;
        for(ScoreDoc scoreDoc : scoreDocs) {
            Document doc = searcher.doc(scoreDoc.doc);
            System.out.println(doc.get("id"));
            System.out.println(doc.get("text"));
            System.out.println(doc.get("content"));
        }
        //using collect

    }



    public void nup() throws Exception {
        FSDirectory directory = FSDirectory.open(getPath());
        DirectoryReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);
        //other query build
        WildcardQuery wildcardQuery = new WildcardQuery(new Term("content", "*山*"));

        //the other results
        TopDocs results = searcher.search(wildcardQuery, 10);
        System.out.println(results.totalHits);
        ScoreDoc[] scoreDocs = results.scoreDocs;
        for(ScoreDoc scoreDoc : scoreDocs) {
            Document doc = searcher.doc(scoreDoc.doc);
            System.out.println(doc);
        }

    }
    /**Field机制详见{@see org.apache.lucene.document.Field}*/
    /**Query机制详见{@see org.apache.lucene.search.Query}*/


    /**
     * search and score: Lucene默认是按照评分机制对每个Document进行打分，然后在返回结果中按照得分进行降序排序
     * https://yq.aliyun.com/articles/45389
     *
     * 1 query查询在document中出现次数越多的score越大
     * 2 field 设置 weight 新版貌似没有了?
     * 3 query 设置 weight ?
     *
     * 尚待研究
     * http://lucene.apache.org/core/7_7_0/core/org/apache/lucene/search/package-summary.html#search
     *
     * SortFiled.SCORE Sort by document score (relevance){根据文档评分(相关性)排序}
     *
     */

    /**
     * sort
     * {@see org.apache.lucene.search}
     *
     */


    /**
     * page
     * search currentPage*pageSize 查询本页数据和这一页之前的所有数据，只取本页数据
     */
    //collector

}

