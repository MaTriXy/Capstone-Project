package com.nunez.bookito.bookLists;

import android.app.Application;

import com.nunez.bookito.BookitoApp;
import com.nunez.bookito.R;
import com.nunez.bookito.entities.Book;
import com.nunez.bookito.mvp.BaseContract;
import com.nunez.bookito.repositories.FirebaseNodes;
import com.nunez.bookito.repositories.FirebaseNodes.BOOK_LISTS;
import com.nunez.bookito.repositories.FirebaseRepo;

/**
 * Created by paulnunez on 3/26/17.
 */

class BookListInteractor implements BookListContract.Interactor {
  private static final String TAG = "BookListInteractor";

  private Application       app;
  private BookListPresenter presenter;

  public BookListInteractor(Application app) {
    this.app = (BookitoApp) app;
  }

  @Override
  public void getBookFromList(@BOOK_LISTS String bookListName) {
    presenter.SendDbReferenceToView(FirebaseRepo.getBooksFromNodeReference(bookListName));
  }

  @Override
  public void moveBookTo(@BOOK_LISTS String currentList, @BOOK_LISTS String listToMoveTo, Book book) {
    FirebaseRepo.moveBook(currentList, listToMoveTo, book);

    FirebaseRepo.updateWidget(app);
    presenter.displayMessage(app.getString(R.string.book_list_activity_msg_book_moved_to,
        listToMoveTo));
  }

  @Override
  public void deleteBook(@BOOK_LISTS String bookListName, Book book) {
    FirebaseRepo.deleteBook(bookListName, String.valueOf(book.getId()));
    if(bookListName.equals(FirebaseNodes.WISHLIST)) FirebaseRepo.updateWidget(app);
    presenter.displayMessage(app.getResources()
        .getString(R.string.book_lists_activity_msg_deleted_book));
  }

  @Override
  public void setPresenter(BaseContract.BasePresenter presenter) {
    this.presenter = (BookListPresenter) presenter;
  }
}
