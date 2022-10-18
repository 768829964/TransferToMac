/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.ctakes.rest.util;

import org.apache.ctakes.drugner.type.*;
import org.apache.ctakes.rest.service.CuiResponse;
import org.apache.ctakes.typesystem.type.textsem.*;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Extracts desired Annotations from a jcas, returning information as a map containing {@link CuiResponse}.
 *
 * Created by tmill on 12/20/18.
 */
final public class JCasParser {

   final private List<Class<? extends Annotation>> semClasses;

   /**
    * Initializes a list indicating which Annotation types are of interest.
    */
   public JCasParser(){
      semClasses = Arrays.asList(

            // CUI types:
            DiseaseDisorderMention.class,
            SignSymptomMention.class,
            ProcedureMention.class,
            AnatomicalSiteMention.class,
            MedicationMention.class,
            EventMention.class,
            EntityMention.class,
            IdentifiedAnnotation.class,

            // Temporal types:
            TimeMention.class,
            DateAnnotation.class,

            // Drug-related types:
            FractionStrengthAnnotation.class,
            DrugChangeStatusAnnotation.class,
            StrengthUnitAnnotation.class,
            StrengthAnnotation.class,
            RouteAnnotation.class,
            FrequencyUnitAnnotation.class,
            MeasurementAnnotation.class

      );

   }

   /**
    * @param jcas ye olde ...
    * @return A Map, key is annotation class name (type), value is a list of {@link CuiResponse},
    * one for each annotation of interest.
    */
   public Map<String, List<CuiResponse>> parse( final JCas jcas ) { //throws Exception {

      return JCasUtil.select( jcas, Annotation.class ).stream()
                     .filter( a -> semClasses.contains( a.getClass() ) )
                     .map( CuiResponse::new )
                     .collect( Collectors.groupingBy( CuiResponse::getType ) );

   }

}
