function ANC(ancBridge) {
    return {
        populateInto: function (cssIdentifierOfRootElement) {
            $(cssIdentifierOfRootElement).html(Handlebars.templates.anc_detail(ancBridge.getCurrentANC()));
        },

        bindEveryTodoToCommCare: function (cssIdentifierOfRootElement, cssClassOfChildElement) {
            $(cssIdentifierOfRootElement).on("click", cssClassOfChildElement, function (event) {
                ancBridge.delegateToCommCare($(this).data("caseid"), $(this).data("form"));
            });
        }
    };
}

function ANCBridge() {
    var ancContext = window.context;
    if (typeof ancContext === "undefined" && typeof FakeANCContext !== "undefined") {
        ancContext = new FakeANCContext();
    }

    return {
        getCurrentANC: function () {
            return JSON.parse(ancContext.get());
        },

        delegateToCommCare: function (caseId, formId) {
            ancContext.startCommCare(caseId, formId);
        }
    };
}

function FakeANCContext() {
    return {
        startCommCare: function (caseId, formId) {
            alert("Start CommCare with form " + formId + " on case with caseId: " + caseId);
        },
        get: function () {
            return JSON.stringify({
                    womanName: "Wife 1",
                    caseId: "1234",
                    thaayiCardNumber: "TC Number 1",
                    villageName: "Village 1",
                    subcenter: "SubCenter 1",
                    isHighPriority: true,
                    alerts: [
                        {
                            message: "Alert 1"
                        },
                        {
                            message: "Alert 2"
                        }
                    ],
                    todos: [
                        {
                            message: "IFA Tablet follow-up",
                            formToOpen: "ANC"
                        },
                        {
                            message: "ANC Visit #3",
                            formToOpen: "ANC"
                        }
                    ]
                }
            );
        }
    }
}