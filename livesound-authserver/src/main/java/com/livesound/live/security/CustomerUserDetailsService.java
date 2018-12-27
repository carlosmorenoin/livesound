package com.livesound.live.security;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class CustomerUserDetailsService implements UserDetailsService {

	@Autowired
	private MongoClient mongoClient;

	@Value("${live.database.name}")
	private String dbName;

	@Value("${live.database.collection}")
	private String collectionName;

	@Override public UserDetails loadUserByUsername(final String email) {
		MongoDatabase database = mongoClient.getDatabase(dbName);
		MongoCollection<Document> usersCollection = database.getCollection(collectionName);

		Document userDocument = usersCollection
				.find(Filters.or(
						Filters.eq(UserProperty.EMAIL.val(), email),
						Filters.eq(UserProperty.USERNAME.val(), email))).first();

		if (userDocument != null) {
			final String password = userDocument.getString(UserProperty.PASSWORD.val());
			final String role = userDocument.getString(UserProperty.ROLE.val());
			return new MongoUserDetails(email, password, new String[] { role });
		}
		return null;
	}

	enum UserProperty {
		USERNAME("userName"), EMAIL("email"), PASSWORD("password"), ROLE("role");

		private final String value;

		UserProperty(String value) {
			this.value = value;
		}

		String val() {
			return this.value;
		}
	}
}
