package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.artsmia.model.Adiacenza;
import it.polito.tdp.artsmia.model.ArtObject;

public class ArtsmiaDAO {

	public void listObjects(Map<Integer,ArtObject>idMap) {
		
		String sql = "SELECT * from objects";
//		List<ArtObject> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				//verifico che l'oggetto nella mappa non ci sia, in tal caso lo inserisco 
				
				if(!idMap.containsKey(res.getInt("object_id"))) {
					
					ArtObject artObj = new ArtObject(res.getInt("object_id"), res.getString("classification"), res.getString("continent"), 
							res.getString("country"), res.getInt("curator_approved"), res.getString("dated"), res.getString("department"), 
							res.getString("medium"), res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"), 
							res.getString("rights_type"), res.getString("role"), res.getString("room"), res.getString("style"), res.getString("title"));
					
					idMap.put(res.getInt("object_id"), artObj);
					
				}
				
				
				
				
				
			}
			conn.close();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
	}

	public int getPeso(ArtObject a1, ArtObject a2) {
		String sql = "SELECT COUNT(*) AS c FROM exhibition_objects AS eo1, exhibition_objects AS eo2 WHERE eo1.exhibition_id = eo2.exhibition_id AND eo1.object_id =? and eo2.object_id =?";
		
		int peso =0;
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, a1.getId());
			st.setInt(2, a2.getId());
			
			ResultSet res = st.executeQuery();
			
			if(res.next()) {
				peso = res.getInt("c");
				conn.close();
			}else {
				return -1;
			}
			
			conn.close();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
		
		return peso;
	}
	
	public List<Adiacenza> getAdiacenze(){
		
		String sql = "SELECT eo1.object_id, eo2.object_id, COUNT(*) AS c " + 
				"FROM exhibition_objects AS eo1, exhibition_objects AS eo2 " + 
				"WHERE eo1.exhibition_id = eo2.exhibition_id " + 
				"AND eo1.object_id > eo2.object_id " + 
				"GROUP BY eo1.object_id, eo2.object_id";
		
		List<Adiacenza> ad = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			
			while (res.next()) {
				
				Adiacenza a = new Adiacenza(res.getInt("eo1.object_id"),res.getInt("eo2.object_id"), res.getInt("c"));
				
				ad.add(a);
					
				}	
			
			conn.close();
			
			return ad;
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
		return null;
	}
	
	
	
	
	
}
