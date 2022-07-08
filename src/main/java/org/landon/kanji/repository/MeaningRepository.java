package org.landon.kanji.repository;

import org.landon.kanji.model.KanjiMeaning;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface MeaningRepository extends MongoRepository<KanjiMeaning, String> {
    @Query("{literal: '?0'}")
    KanjiMeaning findMeaningByLiteral(String literal);

    @Query("{ 'meaning.en': '?0'}")
    KanjiMeaning[] findMeaningByEnMeaning(String meaning);
}
