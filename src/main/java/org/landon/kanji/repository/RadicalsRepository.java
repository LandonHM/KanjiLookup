package org.landon.kanji.repository;

import org.landon.kanji.model.KanjiRadicals;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface RadicalsRepository extends MongoRepository<KanjiRadicals, String> {
    @Query("{kanji: '?0'}")
    KanjiRadicals findRadicalsByKanji(String kanji);
}
