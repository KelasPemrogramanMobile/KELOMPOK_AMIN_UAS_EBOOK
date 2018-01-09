package com.odilogy.ebookstore.object;

/**
 * Item is class to construct item for displayed in listview
 * it could be book list or category list
 * @author odilogy@gmail.com
 * */
public class Item {
	
	/** id for identifier that hold book_id retrieve from json response (server)*/
	private int id;
	/** title that hold book title*/
	private String title;
	/** description*/
	private String description;
	/** filename for book */
	private String file;
	/** meta that hold category title */
	private String meta;
	/** external link for download file, if exist*/
	private String externalLink;
	
	/**
	 * Empty constructor
	 * */
	public Item(){
		
	}
	
	/**
	 * Constructor
	 * @param id the category id
	 * @param meta the category name
	 * */
	public Item(int id,String meta){
		this.id = id;
		this.meta = meta;
		this.title = null;
	}
	
	/**
	 * Constructor
	 * @param id book id
	 * @param title book title
	 * @param description description
	 * @param filename book's filename
	 * @param meta book category
	 * */
	public Item(int id,String title,String description,String file,String meta){
		this.id = id;
		this.title = title;
		this.description = description;
		this.file = file;
		this.meta = meta;
	}
	
	/**
	 * @return book_id
	 * */
	public int getId() {
		return id;
	}
	
	/**
	 * set book id
	 * */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * @return book_title
	 * */
	public String getTitle() {
		return title;
	}
	
	/**
	 * set book_title
	 * */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * @return book_description
	 * */
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * @return book_filename
	 * */
	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}
	
	/**
	 * @return book_category or category itself
	 * */
	public String getMeta() {
		return meta;
	}

	public void setMeta(String meta) {
		this.meta = meta;
	}
	
	/**
	 * @return string externalLink if book use external link (not inside API) to download the file
	 * */
	public String getExternalLink() {
		return externalLink;
	}

	public void setExternalLink(String externalLink) {
		this.externalLink = externalLink;
	}
	

}
