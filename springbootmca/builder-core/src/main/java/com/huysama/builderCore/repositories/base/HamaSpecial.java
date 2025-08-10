package com.huysama.builderCore.repositories.base;


import com.huysama.builderDto.dto.core.HamaFilter;
import com.huysama.builderDto.dto.core.ParamBetween;
import com.huysama.builderDto.exception.CoreException;
import com.huysama.builderDto.util.HamaUtil;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.convert.QueryByExamplePredicateBuilder;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HamaSpecial {


    public static <T> Specification<T> between(Map<String, ParamBetween> bw, T obj) {
        return (root, query, builder) -> {
            final List<Predicate> predicates = new ArrayList<>();
            for (Map.Entry<String, ParamBetween> entry : bw.entrySet()) {
                String fieldName = entry.getKey();
                ParamBetween bt = entry.getValue();
                if (bt.getStart() != null)
                    predicates.add(gThanOrEq(root, builder, fieldName, bt.getStart()));
                if (bt.getEnd() != null)
                    predicates.add(lThanOrEq(root, builder, fieldName, bt.getEnd()));
            }
            predicates.add(QueryByExamplePredicateBuilder.getPredicate(root, builder, Example.of(obj)));
            return builder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    public static <T> Specification<T> between(Map<String, ParamBetween> bw) {
        return (root, query, builder) -> {
            final List<Predicate> predicates = new ArrayList<>();
            for (Map.Entry<String, ParamBetween> entry : bw.entrySet()) {
                String fieldName = entry.getKey();
                ParamBetween bt = entry.getValue();
                if (bt.getStart() != null)
                    predicates.add(gThanOrEq(root, builder, fieldName, bt.getStart()));
                if (bt.getEnd() != null)
                    predicates.add(lThanOrEq(root, builder, fieldName, bt.getEnd()));
            }
            return builder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    public static <T, B> Specification<T> in(Map<String, List<B>> mapIn) {
        Specification<T> specificationCAUSEIN = null;
        for (Map.Entry<String, List<B>> entry : mapIn.entrySet()) {
            List<B> listIN = entry.getValue();
            String field = entry.getKey();
            for (List<B> itemIN : HamaUtil.partition(listIN, 999)) {
                Specification<T> specification = (root, query, builder) -> root.get(field).in(itemIN);
                if (specificationCAUSEIN == null) specificationCAUSEIN = specification;
                else specificationCAUSEIN = specificationCAUSEIN.or(specification);
            }
        }
        return specificationCAUSEIN;
    }

    public static <T, B> Specification<T> inand(Map<String, List<B>> mapIn) {
        Specification<T> specificationCAUSEIN = null;
        for (Map.Entry<String, List<B>> entry : mapIn.entrySet()) {
            List<B> listIN = entry.getValue();
            String field = entry.getKey();
            for (List<B> itemIN : HamaUtil.partition(listIN, 999)) {
                Specification<T> specification = (root, query, builder) -> root.get(field).in(itemIN);
                if (specificationCAUSEIN == null) specificationCAUSEIN = specification;
                else specificationCAUSEIN = specificationCAUSEIN.and(specification);
            }
        }
        return specificationCAUSEIN;
    }

    public static <T, B> Specification<T> notIn(Map<String, List<B>> mapNotIn) {
        Specification<T> specificationCAUSEIN = null;
        for (Map.Entry<String, List<B>> entry : mapNotIn.entrySet()) {
            List<B> listIN = entry.getValue();
            String field = entry.getKey();
            for (List<B> itemIN : HamaUtil.partition(listIN, 999)) {
                Specification<T> specification = (root, query, builder) -> root.get(field).in(itemIN).not();
                if (specificationCAUSEIN == null) specificationCAUSEIN = specification;
                else specificationCAUSEIN = specificationCAUSEIN.or(specification);
            }
        }
        return specificationCAUSEIN;
    }

    public static <T> Specification<T> subQuery(HamaFilter<T> smFilter, Class<T> tClass) {
        return (root, query, builder) -> {
//            final List<Predicate> predicates = new ArrayList<>();
            Subquery<T> subquery = query.subquery(tClass);
            Root<T> subqueryRoot = subquery.from(tClass);
//            Subquery<T> select = subquery.select(subqueryRoot);
            Specification<T> specification = builderSMFilter(smFilter, tClass);
            subquery.select(subqueryRoot).where(specification.toPredicate(root, query, builder));
            return builder.exists(subquery);
        };
    }

    public static <T> Specification<T> subQuery(List<HamaFilter<T>> subQueries, Class<T> tClass) {
        Specification<T> rootSpecial = null;
        for (HamaFilter<T> subQuery : subQueries) {
            Specification<T> subSpecial = subQuery(subQuery, tClass);
            if (rootSpecial == null) rootSpecial = subSpecial;
            else rootSpecial.and(subSpecial);
        }
        return rootSpecial;

    }

    public static <T> Specification<T> isNull(String field)  {
        return (root, query, builder) -> {
            final List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.isNull(root.get(field)));
            return builder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
    public static <T> Specification<T> isNull(List<String> fields) {
        Specification<T> root = null;
        for (String field : fields) {
            if (root == null) root = isNull(field);
            else root.and(isNull(field));
        }
        return root;
    }

    public static <T> Specification<T> isNotNull(String field) throws CoreException {
        return (root, query, builder) -> {
            final List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.isNotNull(root.get(field)));
            return builder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    public static <T> Specification<T> like(String key, String value) {
        return (root, query, builder) -> {
            final List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.like(root.get(key), value));
            return builder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    public static <T> Specification<T> like(Map<String, String> isLike) {
        Specification<T> root = null;
        for (Map.Entry<String, String> entry : isLike.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            Specification<T> like = like(key, value);
            if (root == null) root = like;
            else root.and(like);
        }
        return root;
    }

    public static <T> Specification<T> notLike(String key, String value) {
        return (root, query, builder) -> {
            final List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.notLike(root.get(key), value));
            return builder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    public static <T> Specification<T> notLike(Map<String, String> isLike) {
        Specification<T> root = null;
        for (Map.Entry<String, String> entry : isLike.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            Specification<T> like = notLike(key, value);
            if (root == null) root = like;
            else root.and(like);
        }
        return root;
    }


    public static <T> Specification<T> notEqual(String key, Object value) {
        return (root, query, builder) -> {
            final List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.notEqual(root.get(key), value));
            return builder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    public static <T> Specification<T> notEqual(Map<String, Object> isLike) {
        Specification<T> root = null;
        for (Map.Entry<String, Object> entry : isLike.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            Specification<T> like = notEqual(key, value);
            if (root == null) root = like;
            else root.and(like);
        }
        return root;
    }

    private static <T> T clone(Class<T> tClass) {
        return HamaUtil.parse("{}", tClass);
    }

    public static <T> Specification<T> builderSMFilter(HamaFilter<T> smFilter, Class<T> tClass) {
        T entity = HamaUtil.parse(HamaUtil.stringify(smFilter.getFilter()), tClass);
        if (entity == null) entity = clone(tClass);
        // begin build example
        Specification<T> rootSpecial = HamaSpecial.example(entity);
        // beginBuild Between
        Map<String, ParamBetween> bw = smFilter.getBw();
        if (bw != null) rootSpecial = rootSpecial.and(HamaSpecial.between(bw));
        // beginBuild cause in
        Map<String, List<Object>> in = smFilter.getIN();
        if (in != null) rootSpecial = rootSpecial.and(HamaSpecial.in(in));
        // beginBuild cause in and
        Map<String, List<Object>> inand = smFilter.getINAnd();
        if (inand != null) rootSpecial = rootSpecial.and(HamaSpecial.inand(inand));
        // beginBuild cause not in
        Map<String, List<Object>> notIn = smFilter.getNotIN();
        if (notIn != null) rootSpecial = rootSpecial.and(HamaSpecial.notIn(notIn));
        // beginBuild subQueries
        List<HamaFilter<T>> subQueries = smFilter.getSubQueries();
        if (subQueries != null) {
            rootSpecial = rootSpecial.and(subQuery(subQueries, tClass));
        }
        // beginBuild like
        Map<String, String> like = smFilter.getIsLike();
        if (like != null) rootSpecial = rootSpecial.and(like(like));
        // beginBuild not like
        Map<String, String> notLike = smFilter.getIsNLike();
        if (notLike != null) rootSpecial = rootSpecial.and(notLike(notLike));

        List<String> isNulls = smFilter.getIsNull();
        if (isNulls != null){
            rootSpecial = rootSpecial.and(isNull(isNulls));
        }
        if (notLike != null) rootSpecial = rootSpecial.and(notLike(notLike));

        Map<String, Object> not = smFilter.getNotEqual();
        if (not != null) rootSpecial = rootSpecial.and(notEqual(not));

        Map<String,String> equalIgnoreCaseFilter = smFilter.getEqualIgnoreCase();
        if(equalIgnoreCaseFilter != null) {
            rootSpecial = rootSpecial.and(equalIgnoreCase(equalIgnoreCaseFilter));
        }

        Map<String, String[]> sort = smFilter.getSort();
        if (sort != null) {
            Sort sortObj = buildSort(sort);
            rootSpecial = rootSpecial.and((root, query, builder) -> {
                query.orderBy(toJpaOrders(sortObj, root, builder));
                return null;
            });
        }

        return rootSpecial;
    }

    private static List<Order> toJpaOrders(Sort sort, Root<?> root, CriteriaBuilder builder) {
        List<Order> orders = new ArrayList<>();
        for (Sort.Order order : sort) {
            Path<Object> path = root.get(order.getProperty());
            if (order.isAscending()) {
                orders.add(builder.asc(path));
            } else {
                orders.add(builder.desc(path));
            }
        }
        return orders;
    }
    public static <Y extends Comparable<? super Y>> Predicate gThanOrEq(Root<?> root, CriteriaBuilder builder, String fieldName, Object value) {
        Path<Y> path = root.get(fieldName);
        Y castedValue = (Y) value;
        return builder.greaterThanOrEqualTo(path, castedValue);
    }

    public static <Y extends Comparable<? super Y>> Predicate lThanOrEq(Root<?> root, CriteriaBuilder builder, String fieldName, Object value) {
        Path<Y> path = root.get(fieldName);
        Y castedValue = (Y) value;
        return builder.lessThanOrEqualTo(path, castedValue);
    }


    public static <T> Specification<T> example(T obj) {
        Specification<T> specificationRoot = (root, query, builder) -> {
            final List<Predicate> predicates = new ArrayList<>();
            Predicate predicate = QueryByExamplePredicateBuilder.getPredicate(root, builder, Example.of(obj));
            if (predicate != null) {
                predicates.add(predicate);
            }
            return builder.and(predicates.toArray(new Predicate[0]));
        };
        return specificationRoot;
    }

    public static Sort buildSort(Map<String, String[]> sort) {
        String[] ASC = sort.get("ASC");
        if (ASC != null) return Sort.by(Sort.Direction.ASC, ASC);
        String[] DESC = sort.get("DESC");
        if (DESC != null) return Sort.by(Sort.Direction.DESC, DESC);
        return Sort.unsorted();
    }

    public static <Y extends Comparable<? super Y>> Predicate equalIgnoreCase(Root<?> root,CriteriaBuilder builder, String fieldName, String value) {
        final List<Predicate> predicates = new ArrayList<>();
        String normalizedValue = HamaUtil.removeVNAccent(value);
        Expression<String> fieldExpression = builder.lower(root.get(fieldName));
        predicates.add(builder.equal(fieldExpression, normalizedValue.toLowerCase()));
        return builder.and(predicates.toArray(new Predicate[0]));
    }

    public static <T> Specification<T> equalIgnoreCase(Map<String, String> filter) {
        Specification<T> root = null;
        for (Map.Entry<String, String> entry : filter.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            Specification<T> like = ignoreCase(key, value);
            if (root == null) root = like;
            else root.and(like);
        }
        return root;
    }
    public static <T> Specification<T> ignoreCase(String key, String value) {
        return (root, query, builder) -> {
            final List<Predicate> predicates = new ArrayList<>();
            String normalizedValue = HamaUtil.removeVNAccent(value);
            Expression<String> fieldExpression = builder.lower(root.get(key));
            predicates.add(builder.equal(fieldExpression, normalizedValue.toLowerCase()));
            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
