package com.huysama.builderCore.repositories.base;

import com.huysama.builderDto.dto.core.HamaFilter;
import com.huysama.builderDto.exception.CoreException;
import com.huysama.builderDto.util.HamaUtil;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@NoRepositoryBean
public interface HamaRepository<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
    default Class<T> getEntityClass() {
        ParameterizedType genericInterface = (ParameterizedType) ((Class) getClass().getGenericInterfaces()[0]).getGenericInterfaces()[0];
        return (Class<T>) genericInterface.getActualTypeArguments()[0];
    }

    default List<T> filter(Specification<T> filter) {
        List<T> all = this.findAll(filter);
        return all;
    }


    default List<T> filter(HamaFilter<T> smFilter) {
        List<T> rs;
        Long limit = smFilter.getLimit();
        Specification<T> tSpecification = HamaSpecial.builderSMFilter(smFilter, getEntityClass());
        if (limit != null) {
            int page = 0;
            int size = limit.intValue();
            PageRequest pageRequest = PageRequest.of(page, size);
            rs = this.findAll(tSpecification, pageRequest).getContent();
        } else {
            rs = this.findAll(tSpecification);
        }
        return rs;
    }

    default Page<T> filter(Specification<T> filter, PageRequest pageRequest) {
        return this.findAll(filter, pageRequest);
    }

    default List<T> findAll(Map<String, Object> fieldsMap) {

        try {
            Class<T> clazz = this.getEntityClass();
            T filter = HamaUtil.parse("{}", clazz);
            for (var entry : fieldsMap.entrySet()) {
                String fieldName = entry.getKey();
                Object value = entry.getValue();
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                if (value != null && field.getType().isAssignableFrom(value.getClass())) {
                    field.set(filter, value);
                } else {
                    throw new CoreException(999, "Value_noy_null");
                }
            }
            return this.findAll(Example.of(filter));
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    default List<T> findIn(Map<String, List<Object>> causeIn) {
        try {
            Class<T> clazz = this.getEntityClass();
            HamaFilter<T> smFilter = new HamaFilter<>();
            T filter = HamaUtil.parse("{}", clazz);
            smFilter.setFilter(filter);
            smFilter.setIN(causeIn);
            return this.findAll(HamaSpecial.builderSMFilter(smFilter, clazz));
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    default List<T> findNotIn(Map<String, List<Object>> causeIn) {
        try {
            Class<T> clazz = this.getEntityClass();
            T filter = HamaUtil.parse("{}", clazz);
            return this.findNotIn(filter, causeIn);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    default List<T> findNotIn(T filter, Map<String, List<Object>> causeIn) {
        try {
            Class<T> clazz = this.getEntityClass();
            HamaFilter<T> smFilter = new HamaFilter<>();
            smFilter.setFilter(filter);
            smFilter.setNotIN(causeIn);
            return this.findAll(HamaSpecial.builderSMFilter(smFilter, clazz));
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    default List<T> findIn(T filter, Map<String, List<Object>> causeIn) {
        try {
            Class<T> clazz = this.getEntityClass();
            HamaFilter<T> smFilter = new HamaFilter<>();
            smFilter.setFilter(filter);
            smFilter.setIN(causeIn);
            return this.findAll(HamaSpecial.builderSMFilter(smFilter, clazz));
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    default T findOne(Map<String, Object> fieldsMap) throws CoreException {
        try {
            Class<T> clazz = this.getEntityClass();
            T filter = HamaUtil.parse("{}", clazz);
            for (var entry : fieldsMap.entrySet()) {
                String fieldName = entry.getKey();
                Object value = entry.getValue();
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                if (value != null && field.getType().isAssignableFrom(value.getClass())) {
                    field.set(filter, value);
                } else {
                    throw new CoreException(999, "Value_noy_null");
                }
            }
            List<T> all = this.findAll(Example.of(filter));
            if (all.isEmpty()) throw new CoreException(999, "not_found_data");
            if (all.size() != 1) throw new CoreException(999, "data_is_duplicate");
            return all.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CoreException(999, e.getMessage());
        }
    }

    default List<T> findAll(T t) {
        try {
            return this.findAll(Example.of(t));
        } catch (Exception e) {
            e.printStackTrace();
            throw new CoreException(999, e.getMessage());
        }
    }
}
