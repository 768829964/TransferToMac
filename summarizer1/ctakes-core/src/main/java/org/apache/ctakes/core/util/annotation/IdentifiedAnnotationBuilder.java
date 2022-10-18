package org.apache.ctakes.core.util.annotation;


import org.apache.ctakes.core.util.Pair;
import org.apache.ctakes.typesystem.type.constants.CONST;
import org.apache.ctakes.typesystem.type.refsem.UmlsConcept;
import org.apache.ctakes.typesystem.type.textsem.IdentifiedAnnotation;
import org.apache.log4j.Logger;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author SPF , chip-nlp
 * @version %I%
 * @since 9/26/2020
 */
final public class IdentifiedAnnotationBuilder {

   static private final Logger LOGGER = Logger.getLogger( "IdentifiedAnnotationBuilder" );
   static private final Pair<Integer> NULL_SPAN = new Pair<>( -1, -1 );

   private Pair<Integer> _textSpan = NULL_SPAN;
   private SemanticGroup _group = SemanticGroup.UNKNOWN;
   private SemanticTui _type = SemanticTui.UNKNOWN;
   private final Collection<SemanticTui> _tuis = EnumSet.noneOf( SemanticTui.class );
   private String _cui = "";
   private String _prefText = "";
   private final Map<String, Collection<String>> _schemaCodes = new HashMap<>();


   /**
    * @param begin text span character index
    * @param end   text span character index
    * @return this builder
    */
   public IdentifiedAnnotationBuilder span( final int begin, final int end ) {
      return span( new Pair<>( begin, end ) );
   }

   /**
    * @param textSpan text span character indices
    * @return this builder
    */
   public IdentifiedAnnotationBuilder span( final Pair<Integer> textSpan ) {
      _textSpan = textSpan;
      return this;
   }

   /**
    * @param semanticGroup for the annotation
    * @return this builder
    */
   public IdentifiedAnnotationBuilder group( final SemanticGroup semanticGroup ) {
      _group = semanticGroup;
      return this;
   }

   /**
    * @param semanticGroup name for the annotation
    * @return this builder
    */
   public IdentifiedAnnotationBuilder group( final String semanticGroup ) {
      return group( SemanticGroup.getGroup( semanticGroup ) );
   }

   /**
    * @param semanticType for the annotation
    * @return this builder
    */
   public IdentifiedAnnotationBuilder type( final SemanticTui semanticType ) {
      _type = semanticType;
      return this;
   }

   /**
    * @param semanticType name for the annotation
    * @return this builder
    */
   public IdentifiedAnnotationBuilder type( final String semanticType ) {
      return type( SemanticTui.getTui( semanticType ) );
   }

   /**
    * @param tui representing the primary semantic type.
    * @return this builder
    */
   public IdentifiedAnnotationBuilder tui( final String tui ) {
      return type( SemanticTui.getTui( tui ) );
   }

   /**
    * @param semanticType representing possibly secondary semantic type.
    * @return this builder
    */
   public IdentifiedAnnotationBuilder addTui( final SemanticTui semanticType ) {
      _tuis.add( semanticType );
      return this;
   }

   /**
    * @param tui representing possibly secondary semantic type.
    * @return this builder
    */
   public IdentifiedAnnotationBuilder addTui( final String tui ) {
      return addTui( SemanticTui.getTui( tui ) );
   }

   /**
    * @param cui concept unique identifier
    * @return this builder
    */
   public IdentifiedAnnotationBuilder cui( final String cui ) {
      _cui = cui;
      return this;
   }

   /**
    * @param text preferred
    * @return this builder
    */
   public IdentifiedAnnotationBuilder preferredText( final String text ) {
      _prefText = text;
      return this;
   }

   /**
    * Can be used multiple times
    *
    * @param schema name of an encoding schema.  e.g. SNOMEDCT_US
    * @param code   code for this annotation in that schema
    * @return this builder
    */
   public IdentifiedAnnotationBuilder addSchemaCode( final String schema, final String code ) {
      _schemaCodes.computeIfAbsent( schema, s -> new HashSet<>() ).add( code );
      return this;
   }


   /**
    * @param textSpan  -
    * @param docLength -
    * @return false if the span is unspecified, reversed, or outside the document text.
    */
   static private boolean isSpanValid( final Pair<Integer> textSpan, final int docLength ) {
      if ( textSpan.equals( NULL_SPAN ) ) {
         LOGGER.error( "A Text Span must be specified to build an IdentifiedAnnotation." );
         return false;
      }
      if ( textSpan.getValue1() >= textSpan.getValue2() ) {
         LOGGER.error( "The Text Span ("
                       + textSpan.getValue1() + "," + textSpan.getValue2()
                       + ") is poorly formed.  A valid text span is required to build an IdentifiedAnnotation." );
         return false;
      }
      if ( textSpan.getValue1() < 0 || textSpan.getValue2() >= docLength ) {
         LOGGER.error( "The Text Span ("
                       + textSpan.getValue1() + "," + textSpan.getValue2()
                       + ") must be within the document text bounds (0," + docLength
                       + ") to build an IdentifiedAnnotation." );
         return false;
      }
      return true;
   }


   /**
    * @param jcas          ye olde ...
    * @param cui           -
    * @param type          -
    * @param preferredText may be empty
    * @param schema        may be empty
    * @param code          may be empty
    * @return a UmlsConcept for the annotation
    */
   static private UmlsConcept createUmlsConcept( final JCas jcas,
                                                 final String cui,
                                                 final SemanticTui type,
                                                 final String preferredText,
                                                 final String schema,
                                                 final String code ) {
      final UmlsConcept umlsConcept = new UmlsConcept( jcas );
      umlsConcept.setCui( cui );
      if ( type != SemanticTui.UNKNOWN ) {
         umlsConcept.setTui( type.name() );
      }
      if ( !preferredText.isEmpty() ) {
         umlsConcept.setPreferredText( preferredText );
      }
      if ( !schema.isEmpty() ) {
         umlsConcept.setCodingScheme( schema );
         umlsConcept.setCode( code );
      }
      return umlsConcept;
   }


   /**
    * Builds the IdentifiedAnnotation and stores it in the jCas.  The same as .build( jcas )
    *
    * @param jcas ye olde ...
    * @return an IdentifiedAnnotation with properties specified or null if the cui or span are illegal.
    */
   public IdentifiedAnnotation put( final JCas jcas ) {
      return build( jcas );
   }

   /**
    * Builds the IdentifiedAnnotation and stores it in the jCas.  The same as .put( jcas )
    *
    * @param jcas ye olde ...
    * @return an IdentifiedAnnotation with properties specified or null if the cui or span are illegal.
    */
   public IdentifiedAnnotation build( final JCas jcas ) {
      if ( !isSpanValid( _textSpan, jcas.getDocumentText().length() ) ) {
         return null;
      }
      if ( _cui.isEmpty() ) {
         LOGGER.error( "A Concept Unique Identifier (CUI) must be specified to build an IdentifiedAnnotation." );
         return null;
      }
      if ( _group == SemanticGroup.UNKNOWN && _type != SemanticTui.UNKNOWN ) {
         _group = _type.getGroup();
      }
      if ( _group == SemanticGroup.UNKNOWN && !_tuis.isEmpty() ) {
         final Collection<SemanticGroup> groups = _tuis.stream()
                                                       .map( SemanticTui::getGroup )
                                                       .collect( Collectors.toSet() );
         _group = SemanticGroup.getBestGroup( groups );
      }
      if ( _type == SemanticTui.UNKNOWN && !_tuis.isEmpty() ) {
         _type = _tuis.stream()
                      .filter( t -> t.getGroup() == _group )
                      .min( Comparator.comparing( SemanticTui::name ) )
                      .orElse( SemanticTui.UNKNOWN );
      }
      final IdentifiedAnnotation annotation = _group
            .getCreator()
            .apply( jcas );
      annotation.setTypeID( _group.getCode() );
      annotation.setBegin( _textSpan.getValue1() );
      annotation.setEnd( _textSpan.getValue2() );
      annotation.setDiscoveryTechnique( CONST.NE_DISCOVERY_TECH_EXPLICIT_AE );

      final Collection<UmlsConcept> umlsConcepts = new HashSet<>();
      if ( _schemaCodes.isEmpty() ) {
         umlsConcepts.add( createUmlsConcept( jcas, _cui, _type, _prefText, "", "" ) );
      } else {
         for ( Map.Entry<String, Collection<String>> schemaCodes : _schemaCodes.entrySet() ) {
            for ( String code : schemaCodes.getValue() ) {
               umlsConcepts.add( createUmlsConcept( jcas, _cui, _type, _prefText, schemaCodes.getKey(), code ) );
            }
         }
      }
      if ( !_tuis.isEmpty() ) {
         _tuis.stream()
              .filter( t -> t != _type )
              .filter( t -> t != SemanticTui.UNKNOWN )
              .map( t -> createUmlsConcept( jcas, _cui, t, "", "", "" ) )
              .forEach( umlsConcepts::add );
      }
      final FSArray conceptArr = new FSArray( jcas, umlsConcepts.size() );
      int arrIdx = 0;
      for ( UmlsConcept umlsConcept : umlsConcepts ) {
         conceptArr.set( arrIdx, umlsConcept );
         arrIdx++;
      }
      annotation.setOntologyConceptArr( conceptArr );
      annotation.addToIndexes();
      return annotation;
   }


}
