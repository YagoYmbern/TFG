///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package demography;
//
//import exception.ExceptionFragment;
//import exception.ExceptionGenome;
//import exception.ExceptionIndividual;
//import java.util.ArrayList;
//import parametersSimulation.MutationList;
//import parametersSimulation.ParametersChromosome;
//import parametersSimulation.ParametersGenome;
//
///**
// *
// * @author Administrator
// */
//public class Group extends Demography{
//    public Group(String name, ParametersGenome parametersG)
//    {
//        super(name, parametersG);
//    }
//
//    private ArrayList<Population> runningPopulations = new ArrayList<>();
//    private ArrayList<Population> populations = new ArrayList<>();
//    private int numberIndividualsCurrentGeneration = 0;
//
//
//    /**
//     * Add a new population. Overwrite the parametersGenome of the population by parametersPopulation of the Group
//     * @param p
//     * @throws DemographyException
//     */
//    public void addPopulation(Population p) throws DemographyException {
//        p.setChromosomes(this.getParametersGenome());
//        runningPopulations.add(p);
//        populations.add(p);
//    }
//
//    public void initialize_group() throws ExceptionFragment, ExceptionIndividual, ExceptionGenome
//    {
//        initialize();
//    }
//
//    /**
//     * Initialize all the populations
//     * @throws ExceptionFragment
//     * @throws ExceptionIndividual
//     * @throws ExceptionGenome 
//     */
//    @Override
//    protected void initialize() throws ExceptionFragment, ExceptionIndividual, ExceptionGenome{
//        for (Population p: runningPopulations) {
//            System.out.println("POP " + p.getName());
//            sampleIndividuals.addAll(p.getSampleIndividual());
//            numberIndividualsCurrentGeneration+= p.getCcIndividuals().size();
//        }
//        for(Population p:runningPopulations)
//        {
//            p.setTotal_samples_group(numberIndividualsCurrentGeneration);
//        }
//    }
//
//    /**
//     * Do a new generation
//     * @throws DemographyException
//     */
//    @Override
//    public void nextBackwardGeneration() throws ExceptionIndividual, ExceptionGenome, ExceptionFragment, DemographyException {
//        numberIndividualsCurrentGeneration = 0;
//        for (int p = 0; p < runningPopulations.size(); p++) {
//            runningPopulations.get(p).nextBackwardGeneration();
//            numberIndividualsCurrentGeneration+=runningPopulations.get(p).getCcIndividuals().size();
//        }
//    }
//
//    /**
//     * Get the total number of individuals that still contain non merged fragments
//     * @return
//     */
//    public int getCurrentIndividualsSize()
//    {
//        return numberIndividualsCurrentGeneration;
//    }
//
//    /**
//     * Merge the two populations using the name of the populations. 
//     * After the emissor is merged with the receptor, it disappears from the simulation
//     * @param nameOfReceptor
//     * @param nameOfEmisor
//     * @throws DemographyException
//     */
//    public void mergePopTwoIntoOne(String nameOfReceptor, String nameOfEmisor) throws DemographyException {
//        int populationReceptor = 0;
//        while (!runningPopulations.get(populationReceptor).getName().equals(nameOfReceptor)) {
//            populationReceptor++;
//        }
//        int populationEmisor = 0;
//        while (!runningPopulations.get(populationEmisor).getName().equals(nameOfEmisor)) {
//            populationEmisor++;
//        }
//        runningPopulations.get(populationReceptor).mergeOtherPopulationToThis(runningPopulations.get(populationEmisor));
//// Population popId2 is not active anymore
//        runningPopulations.remove(populationEmisor);
//    }
//
//    /**
//     * Get the population in position popId
//     * @param popName
//     * @return
//     */
//    public Population getPopulation(String popName) {
//        int populationReceptor = 0;
//        while (!populations.get(populationReceptor).getName().equals(popName)) {
//            populationReceptor++;
//        }
//        return populations.get(populationReceptor);
//    }
//
//    @Override
//    public ArrayList<Individual> getSampleIndividual() {
//        return sampleIndividuals;
//    }
//    
//    public static void main(String [] args) throws Exception
//    {
//        ParametersGenome pG = new ParametersGenome();
//        pG.add(new ParametersChromosome(1.25 * Math.pow(10, -7), 1.8 * Math.pow(10, -8), 1000000, new MutationList()));
//        Population p = new Population("POPA", 1, pG);
//        p.setEffectivePopulationSizeParentOneInIndividuals(1000);        
//        
//        Group group = new Group("Group", pG);
//        group.addPopulation(p);
//        
//        Population pB = new Population("POPB", 1, pG);
//        pB.setEffectivePopulationSizeParentOneInIndividuals(5000);    
//        group.addPopulation(pB);
//        
//        group.initialize_group();
//        
//        for(int gen=0;gen<100;gen++)
//        {
//            System.out.println(gen);
//            group.nextBackwardGeneration();
//        }
//        
//        p.mergeOtherPopulationToThis(pB);
//        
//        while(group.getCurrentIndividualsSize()>0)
//        {
//            System.out.println(group.getCurrentIndividualsSize());
//            group.nextBackwardGeneration();            
//        }
//        
//        ArrayList<Individual> individuals = group.getSampleIndividual();
//        
//        for(Individual i:individuals)
//        {
//            System.out.println(i.getGenome(0).getChromosome(0).get(0).getSequences().get(0));
//            System.out.println(i.getGenome(0).getChromosome(1).get(0).getSequences().get(0));
//            System.out.println("");            
//        }
//    }
//}
