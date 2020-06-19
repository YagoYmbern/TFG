///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package event;
//
//import demography.Population;
//import demography.Population_MigrationRate;
//import exception.ExceptionDemography;
//import parametersSimulation.parameters.ModelParameter;
//
//
///**
// *
// * @author oscar
// */
//public class EventEvolutionarySimulatorMigration extends EventEvolutionarySimulator{
//    public EventEvolutionarySimulatorMigration(ModelParameter generation, Population population, Population_MigrationRate [] sourceOfMigrants)
//    {
//        super(generation,population);
//        this.sourceOfMigrants = sourceOfMigrants;
//    }
//
//    private Population_MigrationRate [] sourceOfMigrants;
//
//    @Override
//    public void doEvent() throws ExceptionDemography {
//        getPopulation().setSourceOfMigrants(sourceOfMigrants);
//    }
//    
//    @Override
//    public String toString()
//    {
//        StringBuilder a = new StringBuilder();
//        a.append(getWhenOccurred()).append(" ").append(getPopulation().getName()).append(" ->");
//        for(Population_MigrationRate pm:sourceOfMigrants)
//        {
//            a.append(" ").append(pm.getPopulation().getName()).append(" ").append(pm.getMigration_rate().toString());
//        }        
//        return a.toString();
//    }    
//}
