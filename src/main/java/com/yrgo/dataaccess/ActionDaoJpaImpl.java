package com.yrgo.dataaccess;

import com.yrgo.domain.Action;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
@Repository("actionDao")
public class ActionDaoJpaImpl implements ActionDao{
    @PersistenceContext
    private EntityManager em;

    @Override
    public void create(Action newAction) {
        em.persist(newAction);
    }

    @Override
    public List<Action> getIncompleteActions(String userId) {
       return em.createQuery("select a from Action a WHERE a.actionId=:userId and a.complete=false", Action.class).setParameter("userId", userId).getResultList();

    }

    @Override
    public void update(Action actionToUpdate) throws RecordNotFoundException {
        Action existingAction = em.find(Action.class, actionToUpdate.getActionId());
        if (existingAction == null) {
            throw new RecordNotFoundException();
        }
        em.merge(actionToUpdate);
    }

    @Override
    public void delete(Action oldAction) throws RecordNotFoundException {
        Action action = em.find(Action.class, oldAction.getActionId());
        if (action == null) {
            throw new RecordNotFoundException();
        }
        em.remove(action);

    }
}
