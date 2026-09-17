package com.api.tvmaze.domain.model;

import java.util.List;

public record ShowWithComments(
	 Show show,
     List<Comment> comments    
 ){}
