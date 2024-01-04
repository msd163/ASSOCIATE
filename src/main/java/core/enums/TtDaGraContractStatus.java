package core.enums;

public enum TtDaGraContractStatus {
    NoContract,             // There is no contract for agent in the DAG.
    Request_New,            // Newly added to change with no signing and signed items.
    Request_Signing,        // In Singing phase:        In order to pass ths stage, the agent has to sign others in certain number.
    Request_Verifying,      // In Validation phase:     In order to pass ths stage, the agent has to verify others in certain number.
    Accept_New,             //
    Accept_Signing,         //
    Accept_Verifying,       //
    Accept_Accept,          // The agent singed and verified other successfully and has a valid certification.
    Expired,                // The one or more signs of the agent certification is expired.
}
