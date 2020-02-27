package com.stackroute.keepnote.service;

import com.stackroute.keepnote.exception.NoteNotFoundExeption;
import com.stackroute.keepnote.model.Note;
import com.stackroute.keepnote.model.NoteUser;
import com.stackroute.keepnote.repository.NoteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

/*
* Service classes are used here to implement additional business logic/validation 
* This class has to be annotated with @Service annotation.
* @Service - It is a specialization of the component annotation. It doesn't currently 
* provide any additional behavior over the @Component annotation, but it's a good idea 
* to use @Service over @Component in service-layer classes because it specifies intent 
* better. Additionally, tool support and additional behavior might rely on it in the 
* future.
* */
@Service
public class NoteServiceImpl implements NoteService {

	/*
	 * Autowiring should be implemented for the NoteRepository and MongoOperation.
	 * (Use Constructor-based autowiring) Please note that we should not create any
	 * object using the new keyword.
	 */

	@Autowired
	NoteRepository noteRepository;

	/*
	 * This method should be used to save a new note.
	 */
	public boolean createNote(Note note) {
		try {
			NoteUser noteUser = new NoteUser();

			noteUser.setUserId(note.getNoteCreatedBy());

			List<Note> mylist = noteUser.getNotes();
			mylist = new ArrayList<>();
			mylist.add(note);
			noteUser.setNotes(mylist);
			NoteUser check = noteRepository.insert(noteUser);
			if (check != null)
				return true;
			else
				return false;
		} catch (Exception e) {
			throw e;
		}
	}

	/* This method should be used to delete an existing note. */

	public boolean deleteNote(String userId, int noteId) {
		// Optional<NoteUser> noteUser = noteRepository.findById(userId);
		// if(noteUser.get()!=null)
		// {
		// noteRepository.delete(noteUser.get());
		// return true;
		// }
		// return false;

		try {
			NoteUser noteUser = noteRepository.findById(userId).orElse(null);
			if (noteUser == null)
				return false;
			else {
				List<Note> myList = noteUser.getNotes();
				Iterator<Note> itr = myList.listIterator();
				while (itr.hasNext()) {
					Note note = (Note) itr.next();
					if (note.getNoteId() == noteId)
						itr.remove();
				}
				noteUser.setNotes(myList);
				NoteUser check = noteRepository.save(noteUser);
				if (check != null)
					return true;
				else
					return false;
			}

		} catch (Exception e) {
			throw e;
		}
	}

	/* This method should be used to delete all notes with specific userId. */

	public boolean deleteAllNotes(String userId) {

		try {
			NoteUser noteUser = noteRepository.findById(userId).orElse(null);
			if (noteUser == null)
				return false;
			else {
				List<Note> myList = noteUser.getNotes();
				Iterator<Note> itr = myList.listIterator();
				while (itr.hasNext()) {
					itr.next();
					itr.remove();
				}
				noteUser.setNotes(myList);
				NoteUser check = noteRepository.save(noteUser);
				if (check != null)
					return true;
				else
					return false;
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/*
	 * This method should be used to update a existing note.
	 */
	public Note updateNote(Note note, int id, String userId) throws NoteNotFoundExeption {
		try {
			NoteUser noteUser = noteRepository.findById(userId).orElse(null);
			List<Note> myList = noteUser.getNotes();
			Iterator<Note> itr = myList.listIterator();
			while (itr.hasNext()) {
				Note note1 = (Note) itr.next();
				if (note1.getNoteId() == id) {
					itr.remove();
				}
			}
			myList.add(note);
			noteUser.setNotes(myList);
			noteRepository.save(noteUser);
			return note;
		} catch (NoSuchElementException e) {
			throw new NoteNotFoundExeption("NoteNotFoundException");
		}
	}

	/*
	 * This method should be used to get a note by noteId created by specific user
	 */
	public Note getNoteByNoteId(String userId, int noteId) throws NoteNotFoundExeption {
		try {
			NoteUser noteUser = noteRepository.findById(userId).orElse(null);
			Note answer = new Note();
			List<Note> myList = noteUser.getNotes();
			Iterator<Note> itr = myList.listIterator();
			while (itr.hasNext()) {
				Note note1 = (Note) itr.next();
				if (note1.getNoteId() == noteId) {
					answer = note1;
				}
				break;
			}
			return answer;
		} catch (NoSuchElementException e) {
			throw new NoteNotFoundExeption("Error!");
		}
	}

	/*
	 * This method should be used to get all notes with specific userId.
	 */
	public List<Note> getAllNoteByUserId(String userId) {
		try {
			NoteUser noteUser = noteRepository.findById(userId).orElse(null);
			if (noteUser == null)
				return null;
			else
				return noteUser.getNotes();
		} catch (Exception e) {
			throw e;
		}
	}

}
