/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.ctakes.ytex.web.search;

import java.io.Serializable;

/**
 * Mapped to V_UMLS_FWORD_LOOKUP
 * This table does not have a primary key, so we can't map it with hibernate.
 * We could add a primary key, but the lookups are fairly simple, so
 * we do it with sql.
 * 
 * @author vijay
 */
public class ConceptFirstWord implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String fword;
	private String conceptId;
	private String text;
	public String getFword() {
		return fword;
	}
	public void setFword(String fword) {
		this.fword = fword;
	}
	public String getConceptId() {
		return conceptId;
	}
	public void setConceptId(String cui) {
		this.conceptId = cui;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	@Override
	public String toString() {
		return "UMLSFirstWord [cui=" + conceptId + ", text=" + text + "]";
	}
	

}
