
# ASSOCIATE
### A Simulator for Assessing Soft Security in the Cognitive Internet of Things

---


**ASSOCIATE** is a modular and multi-layered simulation tool developed for evaluating soft security models in the context of the Cognitive Internet of Things (CIoT). It enables researchers to simulate and assess the effectiveness of soft security models, specifically focusing on trust and security in CIoT systems.

## Features

- **Modular Architecture**: ASSOCIATE is designed with a modular architecture consisting of three main components: the Society Generation Module (SGM), Simulator Module (SiM), and World Space (WSM). This modular design allows for independent upgrades and modifications to each module and layer, facilitating adaptability to evolving security requirements.
- **Comprehensive CIoT Environment**: The World Space (WSM) component of ASSOCIATE encompasses four layers: society, transition, trust, and the Internet, providing a comprehensive representation of the CIoT environment. This enables researchers to study the dynamics of social behaviors, trust relationships, and system performance in CIoT systems.
- **Flexible Society Generation**: The Society Generation Module (SGM) allows researchers to create diverse and customized societies by specifying desired characteristics through a configuration file. This empowers security engineers to define various scenarios and evaluate different trust profiles in CIoT simulations.
- **Visualization and Reporting**: ASSOCIATE generates visual representations of evaluation results in the form of charts and graphs. These visualizations provide valuable insights into the performance and effectiveness of soft security models under different scenarios. The results can be saved in image and CSV file formats, facilitating further analysis and comparison.

## Types of Agents in ASSOCIATE
ASSOCIATE is a tool for analysing multi agent system which could model following four types of agents:
- **Honest Agents: Honest agents always provide correct answers to other agents’ requests. They are reliable and trustworthy
- **Adversary Agents: Adversary agents are dishonest and malicious. They intentionally lead requesters to trap states, compromising the security of the system.
- **Neutral Agents: Neutral agents are neither malicious nor reliable. They provide irrelevant and random answers, which can disrupt communication within the system.
- **Hypocrite Agents: Hypocrite agents exhibit different behaviors at different moments with a predefined probability. They can adopt the behaviors of honest agents, adversaries, or neutral agents, depending on the situation.

## Getting Started

### Prerequisites

To run ASSOCIATE, ensure you have the following installed:

- Java Development Kit (JDK) 8 or higher
- Maven for dependency management

### Installation

1. Clone the ASSOCIATE repository to your local machine:
    ```bash  
    git clone https://github.com/msd163/ASSOCIATE.git
    ```

2. Open the cloned project in your preferred Java-based IDE, such as IntelliJ IDEA or Eclipse.

3. Update maven libraries

4. Build and compile the project using the IDE's build tools.

5. Run the compiled project in your IDE's execution environment.

### Usage

1. Launch ASSOCIATE by running the main application:

2. Configure the simulation parameters, rules, and agents according to your research objectives. Refer to the documentation for detailed instructions and examples.

3. Run the simulation and observe the results. Utilize the built-in visualization tools to analyze and interpret the simulation output.

## Contributing

We welcome contributions from the research community to enhance ASSOCIATE's capabilities and expand its applicability in the field of soft security in the CIoT.

## License

ASSOCIATE is released under the GPLv3 License. See the [LICENSE](LICENSE) file for more details.

## Contact

For any questions or inquiries, please contact the development team at [narimani.msd@gmail.com](mailto:narimani.msd@gmail.com).

We appreciate your interest in ASSOCIATE and hope that it serves as a valuable tool for evaluating soft security models and advancing the understanding of trust and security in CIoT systems.

*Note: The GitHub URL for ASSOCIATE is [https://github.com/msd163/ASSOCIATE](https://github.com/msd163/ASSOCIATE).*

## Related Publications

### Article 01: A Comprehensive Soft Security Model for Cognitive Internet of Things

###  https://doi.org/10.1016/j.iot.2023.100858


      @article{ABADI2023100858,
      title = {A comprehensive soft security model for Cognitive Internet of Things},
      journal = {Internet of Things},
      pages = {100858},
      year = {2023},
      issn = {2542-6605},
      doi = {https://doi.org/10.1016/j.iot.2023.100858},
      url = {https://www.sciencedirect.com/science/article/pii/S2542660523001816},
      author = {Masoud Narimani Zaman Abadi and Amir Jalaly Bidgoly and Yaghoub Farjami and Ebrahim Hossein Khani},
      keywords = {Cognitive Internet of Things, Soft security, Trust model, Reinforcement learning algorithm, Maclaurin series},
      abstract = {The Cognitive Internet of Things (CIoT) is a rapidly evolving field that combines artificial intelligence (AI) with the Internet of Things (IoT) ecosystem. By augmenting IoT with AI, objects can sense, perceive, think, and make decisions independently with minimal initial knowledge. These cognitive objects form a society and work towards achieving their goals through cooperation. However, trust is a significant security challenge in such a society. To address this challenge, this paper proposes a soft security approach to model trust in CIoT using Collaborative Multi-Agent Systems (CMAS). Our model introduces an interactive, autonomous, and self-taught agent that can move toward a secure situation without needing a supervisor. We use a state machine to model an insecure ecosystem where agents can behave honestly, adversarially, neutrally, or hypocritically. We define inner trust as a combination of direct and indirect experiences and observations and global trust as the weighted average of inner trust and recommendations. Additionally, we employ the reinforcement learning algorithm to train agents. To evaluate our model, we developed a proprietary tool called ASSOCIATE and assessed our model using data from the Santander Smart City dataset and simulator-generated data. Our evaluations encompass three aspects: the success rate in achieving the goal, the recognition of agents’ behavior, and the quality of the diagnosis.}
      }


