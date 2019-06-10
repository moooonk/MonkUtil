/**
 * 
 */
package com.monk.util.search.lucene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.util.QueryBuilder;

/**
 * Lucene 搜索引擎内存版 已封装常用的搜索，排序搜索，更新，删除接口
 * 注意：所有更新操作均为更新缓存，搜索接口不能立即观察到变化，由一个异步线程定时提交更新
 * 
 * @author huangguanlin
 * 
 *         2019年6月10日
 */
public class LuceneSearchEngine<T> {
	
	private Analyzer analyzer;
	private QueryBuilder parser;
	private ByteBuffersDirectory directory;
	private IndexWriter iwriter;
	private Function<T, Document> buildDoc;
	private AtomicReference<DirectoryReader> reader;
	private AtomicReference<IndexSearcher> searcher;
	
	private ScheduledThreadPoolExecutor executor;
	private AtomicBoolean update;
	
	private static final int COMMIT_DELAY = 20;
	
	/**
	 * 需要自己提供实体类到Document的转换方法。
	 * 模糊匹配需要使用需要再Document中add(){@link org.apache.lucene.document.TextField}索引字段。
	 * {@link org.apache.lucene.document.StringField} 只支持单个词完全匹配，强烈建议add一个此类型的唯一id值用于删除更新操作。
	 * 
	 * @param elements
	 * @param buildDoc
	 * @throws IOException
	 */
	public LuceneSearchEngine(Collection<T> elements, Function<T, Document> buildDoc) throws IOException{
		this.buildDoc = buildDoc;
		directory = new ByteBuffersDirectory();
		analyzer = new StandardAnalyzer();
		parser = new QueryBuilder(analyzer);
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		this.iwriter = new IndexWriter(directory, config);
		update = new AtomicBoolean(false);
		addElement(elements);
		iwriter.commit();
		reader = new AtomicReference<DirectoryReader>(DirectoryReader.open(directory));
		searcher = new AtomicReference<IndexSearcher>(new IndexSearcher(reader.get()));
		executor = new ScheduledThreadPoolExecutor(1, new PoolThreadFactory("LuceneSearchEngine", true, 1));
		executor.scheduleWithFixedDelay(() ->{
			if(update.compareAndSet(true, false)){
				try {
					iwriter.commit();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, 0, COMMIT_DELAY, TimeUnit.SECONDS);
	}
	
	public void addElement(Collection<T> elements){
		try {
			for (T element : elements) {
				Document document = this.buildDoc.apply(element);
				iwriter.addDocument(document);
			}
			update.set(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private IndexSearcher chackAndGetIndexSearcher(){
		DirectoryReader directoryReader = reader.get();
		try {
			DirectoryReader newDirectoryReader = DirectoryReader.openIfChanged(directoryReader);
			if(newDirectoryReader != null && reader.compareAndSet(directoryReader, newDirectoryReader)){
				directoryReader = reader.get();
				searcher.set(new IndexSearcher(directoryReader));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return searcher.get();
	}
	
	/**
	 * 匹配模式依赖分析器解析结果，如果带中文，基本上都是分词匹配，不要用来查询StringField类型的索引
	 * @param field 需要匹配的字段名字
	 * @param queryText 匹配内容
	 * @param page 第几页，从1起
	 * @param pageSize 每页大小
	 * @param sort 排序参数，null就不执行排序搜索
	 * @return List<Document> 匹配上的Document对象
	 * 
	 */
	public List<Document> search(String field, String queryText, int page, int pageSize, Sort sort){
        Query query = parser.createPhraseQuery(field, queryText);
        if(query == null){
        	return Collections.emptyList();
        }
        IndexSearcher isearcher = chackAndGetIndexSearcher();
        try {
        	ScoreDoc startDoc = null;
        	TopDocs topDocs = null;
        	if(page > 1){
        		if(sort != null){
        			topDocs = isearcher.search(query, (page - 1) * pageSize, sort);    				
    			}else {
    				topDocs = isearcher.search(query, (page - 1) * pageSize);    	
				}
        		if(topDocs.scoreDocs.length != 0){
        			startDoc = topDocs.scoreDocs[topDocs.scoreDocs.length - 1];        			
        		}
        	}
    		if(sort != null){
    			topDocs = isearcher.searchAfter(startDoc, query, pageSize, sort);    				
			}else {
				topDocs = isearcher.searchAfter(startDoc, query, pageSize);    	
			}
        	List<Document> result = new ArrayList<Document>(topDocs.scoreDocs.length);
        	for(ScoreDoc scoreDocs : topDocs.scoreDocs){
        		result.add(isearcher.doc(scoreDocs.doc));
        	}
        	return result;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Document> wildCardSearch(String field, String queryText, int page, int pageSize, Sort sort){
        IndexSearcher isearcher = chackAndGetIndexSearcher();
        try {
        	 Term t1 = new Term(field, queryText);
        	 WildcardQuery query = new WildcardQuery(t1);
        	
        	ScoreDoc startDoc = null;
        	TopDocs topDocs = null;
        	if(page > 1){
        		if(sort != null){
        			topDocs = isearcher.search(query, (page - 1) * pageSize, sort);    				
    			}else {
    				topDocs = isearcher.search(query, (page - 1) * pageSize);    	
				}
        		if(topDocs.scoreDocs.length != 0){
        			startDoc = topDocs.scoreDocs[topDocs.scoreDocs.length - 1];        			
        		}
        	}
    		if(sort != null){
    			topDocs = isearcher.searchAfter(startDoc, query, pageSize, sort);    				
			}else {
				topDocs = isearcher.searchAfter(startDoc, query, pageSize);    	
			}
        	List<Document> result = new ArrayList<Document>(topDocs.scoreDocs.length);
        	for(ScoreDoc scoreDocs : topDocs.scoreDocs){
        		result.add(isearcher.doc(scoreDocs.doc));
        	}
        	return result;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 为了防止删错值，强制使用不分词匹配
	 */
	public void deleleElement(String field, String queryText){
		try {
			Query query = new TermQuery(new Term(field, queryText));
			iwriter.deleteDocuments(query);
			update.set(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 为了防止更新错值，强制使用不分词匹配
	 * @param field 需要指定匹配唯一ID用的字段名字
	 */
	public void updateElement(Collection<T> elements, String field){
		try {
			for (T element : elements) {
				Document document = buildDoc.apply(element);
				Term query = new Term(field, document.get(field));
				iwriter.updateDocument(query, document);				
			}
			update.set(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void close(){
		if(reader.get() != null){
			try {
				reader.get().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(iwriter != null){
			try {
				iwriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(directory != null){
			try {
				directory.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(executor != null){
			executor.shutdownNow();
		}
	}
	
	private static class PoolThreadFactory implements ThreadFactory{
		private final ThreadGroup group;
		private final String namePrefix;
		private final Boolean daemon;
		

		public PoolThreadFactory(String namePrefix, Boolean daemon, int index) {
			SecurityManager s = System.getSecurityManager();
			this.group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
			this.namePrefix = String.format("%s-%d", namePrefix, index);
			this.daemon = daemon;
		}
		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(group, r, namePrefix, 0);
			// The newly created thread is initially marked as being a daemon thread if and only if
			// the thread creating it is currently marked as a daemon thread.
			if (this.daemon != null) {
				t.setDaemon(this.daemon.booleanValue());
			}

			if (t.getPriority() != Thread.NORM_PRIORITY) {
				t.setPriority(Thread.NORM_PRIORITY);
			}
			return t;
		}
		
	}
	
}
