/*
 * Copyright (C) 2003-2012 eXo Platform SAS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package exoplatform;

import java.io.Serializable;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.exoplatform.services.cache.CacheService;
import org.exoplatform.services.cache.ExoCache;
import org.exoplatform.services.jcr.RepositoryService;
import org.picocontainer.Startable;

import exoplatform.entity.Author;
import exoplatform.entity.Book;
import exoplatform.entity.Book.CATEGORY;
import exoplatform.entity.User;
import exoplatform.exception.BookNotFoundException;
import exoplatform.exception.DuplicateBookException;

/**
 * Created by The eXo Platform SAS
 * Author : BinhNV
 *          binhnv@exoplatform.com
 * Jul 6, 2012  
 */
public class BookStoreServiceImpl implements Startable, BookStoreService {

  /* Define a exo cache */
  private ExoCache<Serializable, Book> cache;
  /* Define a JCRDataStore object */
  private JCRDataStorage jcrDataStorage;
  
  /**
   * BookStoreService constructor
   * 
   * @param rservice
   * @param cacheService
   * @param dataStorage
   * @throws RepositoryException
   * @throws DuplicateBookException 
   */
  public BookStoreServiceImpl(RepositoryService rservice,
                          CacheService cacheService,
                          JCRDataStorage dataStorage) {
    this.cache = cacheService.getCacheInstance(getClass().getName());
    this.jcrDataStorage = dataStorage;
  }

  /**
   * Init data for datastorage
   */
  public void start() {
    jcrDataStorage.init();
  }

  /* (non-Javadoc)
   * @see org.picocontainer.Startable#stop()
   */
  public void stop() {
    
  }
  
  /**
   * create new database
   */
  public void createDB() {
    try {
      Node authorNode1 = addAuthor("Arthur Conan Doyle", "England", "123456789");
      Node authorNode2 = addAuthor("J. K. Rowling", "England", "987654321");
      Node authorNode3 = addAuthor("Sydney Sheldon", "US", "678912345");
      Node authorNode4 = addAuthor("Christian Bauer", "US", "012345678");
      Node authorNode5 = addAuthor("Mario Puzzo", "Italia", "23451234");
      Node authorNode6 = addAuthor("Victor Hugo", "France", "8760985544");
      Node authorNode7 = addAuthor("Wu Cheng", "China", "8760985544");
      Node authorNode8 = addAuthor("Robert Kiyosaki.", "US", "4353546464");
      Node authorNode9 = addAuthor("Alexandre Dumas", "France", "9203492394");
      Node authorNode10 = addAuthor("Howard E. Wasdin", "US", "9203492394");
      
      Book book = new Book("The Sign of the Four", CATEGORY.NOVEL, "The Sign of the Four" +
                                            "/nBy Sir Arthur Conan Doyle" +
                                            "/nChapter 1" +
                                            "/nThe Science of Deduction" +
                                            "/nSherlock Holmes took his bottle from the corner of the mantel- " +
                                            "piece and his hypodermic syringe from its neat morocco case. " +
                                            "With his long, white, nervous fingers he adjusted the delicate needle, " +
                                            "and rolled back his left shirt-cuff. " +
                                            "For some little time his eyes rested thoughtfully upon the sinewy forearm and wrist " +
                                            "all dotted and scarred with innumerable puncture-marks. " +
                                            "Finally he thrust the sharp point home, pressed down the tiny piston, " +
                                            "and sank back into the velvet-lined arm-chair with a long sigh of satisfaction." +
                                            "Three times a day for many months I had witnessed this performance, " +
                                            "but custom had not reconciled my mind to it. " +
                                            "On the contrary, from day to day I had become more irritable at the sight, " +
                                            "and my conscience swelled nightly within me at the thought that " +
                                            "I had lacked the courage to protest. Again and again I had registered a vow that " +
                                            "I should deliver my soul upon the subject, but there was that in the cool, " +
                                            "nonchalant air of my companion which made him the last man with whom one would care " +
                                            "to take anything approaching to a liberty. His great powers, his masterly manner, " +
                                            "and the experience which I had had of his many extraordinary qualities, " +
                                            "all made me diffident and backward in crossing him." +
                                            "Yet upon that afternoon, whether it was the Beaune which I had taken with my lunch, " +
                                            "or the additional exasperation produced by the extreme deliberation of his manner, " +
                                            "I suddenly felt that I could hold out no longer." +
                                            "/nWhich is it to-day?" + "/nI asked,--\"morphine or cocaine?\""+
                                            "\nHe raised his eyes languidly from the old black-letter volume which he had opened. " +
                                            "\"It is cocaine,\" he said,--\"a seven-per- cent. solution. Would you care to try it?\"");
      Node bookNode1 = addBook(book, authorNode1.getName());
      
      book = new Book("Harry Potter and the Philosopher Stone", CATEGORY.NOVEL, "Harry Potter and the Philosopher's Stone " +
      		                                  "is the first novel in the Harry Potter series written by J. K. Rowling " +
      		                                  "and featuring Harry Potter, a young wizard. " +
      		                                  "It describes how Harry discovers he is a wizard, " +
      		                                  "makes close friends and a few enemies at the Hogwarts School of Witchcraft and Wizardry, " +
      		                                  "and with the help of his friends thwarts an attempted comeback by the evil wizard Lord Voldemort, " +
      		                                  "who killed Harry's parents when Harry was one year old." +
      		                                  "The book, which is J.K. Rowling's debut novel, was published on 26 June 1997 by Bloomsbury in London. " +
      		                                  "In 1998 Scholastic Corporation published an edition for the United States " +
      		                                  "market under the title Harry Potter and the Sorcerer's Stone. " +
      		                                  "The novel won most of the UK book awards that were judged by children, " +
      		                                  "and other awards in the USA. The book reached the top of the New York Times list " +
      		                                  "of best-selling fiction in August 1999, and stayed near the top of that list for much of 1999 and 2000." +
      		                                  "It has been translated into several other languages and has been made into a feature-length film " +
      		                                  "of the same name." +
                                            "Most reviews were very favourable, " +
                                            "commenting on Rowling's imagination, humour, simple, " +
                                            "direct style and clever plot construction, " +
                                            "although a few complained that the final chapters seemed rushed. " +
                                            "The writing has been compared to that of Jane Austen, one of Rowling's favourite authors, " +
                                            "of Roald Dahl, whose works dominated children's stories before the appearance of Harry Potter, " +
                                            "and of the Ancient Greek story-teller Homer. " +
                                            "While some commentators thought the book looked backwards to Victorian " +
                                            "and Edwardian boarding school stories, " +
                                            "others thought it placed the genre firmly in the modern world " +
                                            "by featuring contemporary ethical and social issues.");
      Node bookNode2 = addBook(book, authorNode2.getName());
      
      book = new Book("The Naked Face", CATEGORY.NOVEL, "Judd Stevens is a psychoanalyst faced with the most critical case of his life. " +
      		                                  "If he does not penetrate the mind of a murderer he will find himself arrested " +
      		                                  "for murder or murdered himself..." +
                                            "Two people closely involved with Dr. Stevens have already been killed. " +
                                            "Is one of his patients responsible? Someone overwhelmed by his problems? " +
                                            "A neurotic driven by compulsion");
      Node bookNode3 = addBook(book, authorNode3.getName());
      
      book = new Book("Hibernate in action", CATEGORY.TECHNICAL, "Hibernate practically exploded on the Java scene. " +
      		                                  "Why is this open-source tool so popular? " +
      		                                  "Because it automates a tedious task: persisting your Java objects to a relational database. " +
      		                                  "The inevitable mismatch between your object-oriented code and the relational database requires you " +
      		                                  "to write code that maps one to the other. " +
      		                                  "This code is often complex, tedious and costly to develop. " +
      		                                  "Hibernate does the mapping for you." +
      		                                  "Not only that, Hibernate makes it easy. " +
      		                                  "Positioned as a layer between your application and your database, " +
      		                                  "Hibernate takes care of loading and saving of objects. " +
      		                                  "Hibernate applications are cheaper, more portable, and more resilient to change. " +
      		                                  "And they perform better than anything you are likely to develop yourself." +
      		                                  "\"Hibernate in Action\" carefully explains the concepts you need, " +
      		                                  "then gets you going. It builds on a single example to show you how to use Hibernate in practice, " +
      		                                  "how to deal with concurrency and transactions, how to efficiently retrieve objects and use caching." +
                                            "The authors created Hibernate and they field questions from the Hibernate community every day-" +
                                            "they know how to make Hibernate sing. Knowledge and insight seep out of every pore of this book.");
      Node bookNode4 = addBook(book, authorNode4.getName());
      
      book = new Book("The God Father", CATEGORY.HISTORY, "The Godfather is a 1972 American crime film directed by Francis Ford Coppola " +
      		                                  "and produced by Albert S. Ruddy from a screenplay by Mario Puzo and Coppola. " +
      		                                  "Based on Puzo's 1969 novel of the same name, " +
      		                                  "the film stars Marlon Brando and Al Pacino as the leaders of a powerful New York crime family. " +
      		                                  "The story, spanning the years 1945 to 1955, " +
      		                                  "centers on the ascension of Michael Corleone (Pacino) " +
      		                                  "from reluctant family outsider to ruthless Mafia boss " +
      		                                  "while also chronicling the Corleone family under the patriarch Vito Corleone (Brando).");
      Node bookNode5 = addBook(book, authorNode5.getName());
      
      book = new Book("Les Misérables", CATEGORY.COMICS, "Victor Hugo, " +
      		                                  "in full Victor Marie Hugo (French pronunciation: " +
      		                                  "[viktɔʁ maʁi yɡo]; 26 February 1802 – 22 May 1885) was a French poet, novelist, " +
      		                                  "and dramatist who was the most well-known of all the French Romantic writers. " +
      		                                  "Though regarded in France as one of that country’s greatest poets, " +
      		                                  "he is better known abroad for such novels as Notre-Dame de Paris (1831) and Les Misérables (1862).");
      Node bookNode6 = addBook(book, authorNode6.getName());
      
      book = new Book("Journey to the West", CATEGORY.MANGA, "Journey to the West is one of the Four Great Classical Novels of Chinese literature. " +
      		                                  "It was written in the 16th century during the Ming Dynasty attributed to Wu Cheng'en. " +
      		                                  "In English-speaking countries, the tale is also known as Monkey, " +
      		                                  "the title used for a popular and partial translation by Arthur Waley. " +
      		                                  "The Waley translation has also been published as Adventures of the Monkey God, Monkey: " +
      		                                  "[A] Folk Novel of China, and The Adventures of Monkey, " +
      		                                  "and in a further abridged version for children, Dear Monkey.");
      Node bookNode7 = addBook(book, authorNode7.getName());
      
      book = new Book("Rich Dad, Poor Dad", CATEGORY.MATHS, "The book is the story of a person (the narrator and author) " +
      		                                  "who has two fathers: the first was his biological father – the poor dad - " +
      		                                  "and the other was the father of his childhood best friend, " +
      		                                  "Mike – the rich dad. " +
      		                                  "Both fathers taught the author how to achieve success but with very disparate approaches. " +
      		                                  "It became evident to the author which father's approach made more financial sense. " +
      		                                  "Throughout the book, the author compares both fathers – their principles, ideas, " +
      		                                  "financial practices, and degree of dynamism and how his real father, " +
      		                                  "the poor and struggling but highly educated man, " +
      		                                  "paled against his rich dad in terms of asset building and business acumen.");
      Node bookNode8 = addBook(book, authorNode8.getName());
      
      book = new Book("Three Musketeers", CATEGORY.HISTORY, "The Three Musketeers (French: Les Trois Mousquetaires) " +
      		                                  "is a novel by Alexandre Dumas, first serialized in March–July 1844. " +
      		                                  "Set in the 17th century, it recounts the adventures of a young man named d'Artagnan after " +
      		                                  "he leaves home to travel to Paris, to join the Musketeers of the Guard. " +
      		                                  "D'Artagnan is not one of the musketeers of the title; " +
      		                                  "those are his friends Athos, Porthos, and Aramis, inseparable friends who live by the motto " +
      		                                  "\"all for one, one for all\" (\"tous pour un, un pour tous\") " +
      		                                  "A motto which is first put forth by d'Artagnan");
      Node bookNode9 = addBook(book, authorNode9.getName());
      
      
      book = new Book("SEAL Team Six", CATEGORY.HISTORY, "When the Navy sends their elite, they send the SEALs. " +
      		                                  "When the SEALs send their elite, they send SEAL Team Six" +
                                            "SEAL Team Six is a secret unit tasked with counterterrorism, " +
                                            "hostage rescue, and counterinsurgency. " +
                                            "In this dramatic, behind-the-scenes chronicle, " +
                                            "Howard Wasdin takes readers deep inside the world of Navy SEALS and Special Forces snipers, " +
                                            "beginning with the grueling selection process of Basic Underwater Demolition/SEAL (BUD/S)—" +
                                            "the toughest and longest military training in the world. ");
      Node bookNode10 = addBook(book, authorNode10.getName());

      Node userNode1 = addUser("binhnv", "12345", "Nguyen Vinh Binh", "Hanoi", "123456789");
      Node userNode2 = addUser("huongdt", "54321", "Doan Thu Huong", "Hanoi", "987654321");
      
    } catch (DuplicateBookException e) {
      e.printStackTrace();
    } catch (RepositoryException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * add new book to datastorage
   * 
   * @param bookName
   * @param category
   * @param content
   * @return
   * @throws DuplicateBookException
   */
  public Node addBook(Book book, String authorId) throws DuplicateBookException {
    return jcrDataStorage.addBook(book, authorId);
  }
  
  /**
   * add new author to datastorage
   * 
   * @param authorName
   * @param authorAddress
   * @param authorPhone
   * @return
   */
  public Node addAuthor(String authorName, String authorAddress, String authorPhone) throws DuplicateBookException {
    Author author = new Author(authorName, authorAddress, authorPhone);
    return jcrDataStorage.addAuthor(author);
  }
  
  /**
   * add new user to datastorage
   * 
   * @param username
   * @param password
   * @param fullname
   * @param address
   * @param phone
   * @param nodes
   * @return
   * @throws DuplicateBookException
   */
  public Node addUser(String username, String password, String fullname, String address, String phone) throws DuplicateBookException {
    User user = new User(username, password, fullname, address, phone);
    return jcrDataStorage.addUser(user);
  }
  
  /**
   * search book by book name
   * 
   * @param bookName The name of book
   * @return List<Book> The list of book
   */
  public List<Book> searchBookByName(String bookName) {
    return jcrDataStorage.searchBookByName(bookName);
  }
  
  /**
   * search book by author name
   * 
   * @param authorName The name of author
   * @return List<Book> The list of book
   */
  public List<Book> searchBookByAuthor(String authorName) {
    return jcrDataStorage.searchBookByAuthor(authorName);
  }
  
  /**
   * get book by book's id
   * 
   * @param id The id of book
   * @return
   */
  public Book getBook(String id) {
    Book book = cache.get(id);
    if (book != null) {
      return book;
    }
    return jcrDataStorage.getBook(id);
  }

  /**
   * delete a author by authorName
   * 
   * @param authorName The name of author
   * @throws BookNotFoundException
   */
  public void deleteAuthor(String authorId) throws BookNotFoundException {
    jcrDataStorage.deleteAuthor(authorId);
  }

  /**
   * edit a exist book
   * 
   * @param book The book want to delete
   * @throws BookNotFoundException
   */
  public void editAuthor(Author author) throws BookNotFoundException {
    jcrDataStorage.editAuthor(author);
  }

  public Node addUserReference(String userId, String bookId) throws DuplicateBookException {
    return jcrDataStorage.addUserReference(userId, bookId);
  }
  
  /**
   * get all book exist in datastore
   */
  public List<Book> getAllBook() {
    return jcrDataStorage.getAllBook();
  }
  
  /**
   * get author by book id
   * 
   * @param id
   * @return
   */
  public Author getAuthorByBookId(String bookId) {
    return jcrDataStorage.getAuthorByBookId(bookId);
  }

  public void deleteBook(String id) throws BookNotFoundException {
    jcrDataStorage.deleteBook(id);
  }

  public void editBook(Book book) throws BookNotFoundException {
    jcrDataStorage.editBook(book);
  }

  public Book addBookWithout(Book book) throws DuplicateBookException {
    return jcrDataStorage.addBookWithout(book);
  }
  
}