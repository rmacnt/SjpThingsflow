package com.sjp.domain.usecase;


import com.sjp.data.repository.base.Repository;

final class Helper {

    @SuppressWarnings("unchecked")
    public static void applyParameter(Repository repository, Object parameter) {
        try {
            repository.parameter(parameter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
