import React, { useContext, useEffect, useState, useRef } from "react";
import { FormattedMessage, injectIntl } from "react-intl";
import "../Style.css";
import {
  getFromOpenElisServer,
  postToOpenElisServerJsonResponse,
  convertAlphaNumLabNumForDisplay,
} from "../utils/Utils";
import {
  Heading,
  Form,
  FormLabel,
  TextInput,
  TextArea,
  Checkbox,
  Button,
  Grid,
  Column,
  DatePicker,
  DatePickerInput,
  Stack,
  Section,
  Pagination,
  Select,
  SelectItem,
  Loading,
} from "@carbon/react";
import CustomLabNumberInput from "../common/CustomLabNumberInput";
import DataTable from "react-data-table-component";
import { Formik, Field } from "formik";
import SearchResultFormValues from "../formModel/innitialValues/SearchResultFormValues";
import { AlertDialog, NotificationKinds } from "../common/CustomNotification";
import { NotificationContext } from "../layout/Layout";
import SearchPatientForm from "../patient/SearchPatientForm";
import { ConfigurationContext } from "../layout/Layout";
import config from "../../config.json";

function ResultSearchPage() {
  const [resultForm, setResultForm] = useState({ testResult: [] });
  return (
    <>
      <SearchResultForm setResults={setResultForm} />
      <SearchResults results={resultForm} />
    </>
  );
}

export function SearchResultForm(props) {
  const { notificationVisible, setNotificationVisible, setNotificationBody } =
    useContext(NotificationContext);

  const [tests, setTests] = useState([]);
  const [analysisStatusTypes, setAnalysisStatusTypes] = useState([]);
  const [sampleStatusTypes, setSampleStatusTypes] = useState([]);
  const [loading, setLoading] = useState(false);
  const [searchBy, setSearchBy] = useState({ type: "", doRange: false });
  const [patient, setPatient] = useState({ patientPK: "" });
  const [testSections, setTestSections] = useState([]);
  const [searchFormValues, setSearchFormValues] = useState(
    SearchResultFormValues,
  );

  const componentMounted = useRef(false);

  const setResultsWithId = (results) => {
    if (results.testResult) {
      var i = 0;
      if (results.testResult) {
        results.testResult.forEach((item) => (item.id = "" + i++));
      }
      props.setResults?.(results);
      setLoading(false);
    } else {
      props.setResults?.({ testResult: [] });
      setNotificationBody({
        title: <FormattedMessage id="notification.title" />,
        message: "No results found!",
        kind: NotificationKinds.warning,
      });
      setNotificationVisible(true);
      setLoading(false);
    }
  };

  const handleAdvancedSearch = () => {};

  const getSelectedPatient = (patient) => {
    setPatient(patient);
  };
  useEffect(() => {
    querySearch(searchFormValues);
  }, [patient]);

  const querySearch = (values) => {
    setLoading(true);
    props.setResults({ testResult: [] });

    let accessionNumber =
      values.accessionNumber !== ""
        ? values.accessionNumber
        : values.startLabNo;
    let labNo = accessionNumber !== undefined ? accessionNumber : "";
    const endLabNo = values.endLabNo !== undefined ? values.endLabNo : "";
    values.unitType = values.unitType ? values.unitType : "";

    let searchEndPoint =
      "/rest/ReactLogbookResultsByRange?" +
      "labNumber=" +
      labNo +
      "&upperRangeAccessionNumber=" +
      endLabNo +
      "&patientPK=" +
      patient.patientPK +
      "&testSectionId=" +
      values.unitType +
      "&collectionDate=" +
      values.collectionDate +
      "&recievedDate=" +
      values.recievedDate +
      "&selectedTest=" +
      values.testName +
      "&selectedSampleStatus=" +
      values.sampleStatusType +
      "&selectedAnalysisStatus=" +
      values.analysisStatus +
      "&doRange=" +
      searchBy.doRange +
      "&finished=" +
      true;
    getFromOpenElisServer(searchEndPoint, setResultsWithId);
  };

  const handleSubmit = (values) => {
    querySearch(values);
  };

  const getTests = (tests) => {
    if (componentMounted.current) {
      setTests(tests);
    }
  };

  const getAnalysisStatusTypes = (analysisStatusTypes) => {
    if (componentMounted.current) {
      setAnalysisStatusTypes(analysisStatusTypes);
    }
  };

  const getSampleStatusTypes = (sampleStatusTypes) => {
    if (componentMounted.current) {
      setSampleStatusTypes(sampleStatusTypes);
    }
  };

  const fetchTestSections = (response) => {
    setTestSections(response);
  };

  const submitOnSelect = (e) => {
    var values = { unitType: e.target.value };
    handleSubmit(values);
  };

  useEffect(() => {
    componentMounted.current = true;
    getFromOpenElisServer("/rest/test-list", getTests);
    getFromOpenElisServer(
      "/rest/analysis-status-types",
      getAnalysisStatusTypes,
    );
    getFromOpenElisServer("/rest/sample-status-types", getSampleStatusTypes);
    getFromOpenElisServer("/rest/user-test-sections", fetchTestSections);
    let displayFormType = new URLSearchParams(window.location.search).get(
      "type",
    );
    let doRange = new URLSearchParams(window.location.search).get("doRange");
    setSearchBy({
      type: displayFormType,
      doRange: doRange,
    });
  }, []);

  useEffect(() => {
    let accessionNumber = new URLSearchParams(window.location.search).get(
      "accessionNumber",
    );
    if (accessionNumber) {
      let searchValues = {
        ...searchFormValues,
        accessionNumber: accessionNumber,
      };
      setSearchFormValues(searchValues);
      querySearch(searchValues);
    }
  }, [searchBy]);

  return (
    <>
      {notificationVisible === true ? <AlertDialog /> : ""}
      {loading && <Loading></Loading>}
      <Formik
        initialValues={searchFormValues}
        //validationSchema={}
        onSubmit={handleSubmit}
        onChange
        enableReinitialize={true}
      >
        {({
          values,
          //   errors,
          //   touched,
          handleChange,
          setFieldValue,
          //   handleBlur,
          handleSubmit,
        }) => (
          <Form
            onSubmit={handleSubmit}
            onChange={handleChange}
            //onBlur={handleBlur}
          >
            <Stack gap={2}>
              <Grid>
                <Column lg={16}>
                  <h4>
                    <FormattedMessage id="label.button.search" />
                  </h4>
                </Column>
                {searchBy.type === "order" && (
                  <>
                    <Column lg={6}>
                      <Field name="accessionNumber">
                        {({ field }) => (
                          <CustomLabNumberInput
                            placeholder="Enter Accession No."
                            name={field.name}
                            id={field.name}
                            value={values[field.name]}
                            labelText={
                              <FormattedMessage id="search.label.accession" />
                            }
                            onChange={(e, rawValue) => {
                              setFieldValue(field.name, rawValue);
                            }}
                          />
                        )}
                      </Field>
                    </Column>
                    <Column lg={10} />
                  </>
                )}

                {searchBy.type === "range" && (
                  <>
                    <Column lg={6}>
                      <Field name="startLabNo">
                        {({ field }) => (
                          <TextInput
                            placeholder={"Enter LabNo"}
                            name={field.name}
                            id={field.name}
                            labelText={
                              <FormattedMessage id="search.label.fromaccession" />
                            }
                          />
                        )}
                      </Field>
                    </Column>
                    <Column lg={6}>
                      <Field name="endLabNo">
                        {({ field }) => (
                          <TextInput
                            placeholder={"Enter LabNo"}
                            name={field.name}
                            id={field.name}
                            labelText={
                              <FormattedMessage id="search.label.toaccession" />
                            }
                          />
                        )}
                      </Field>
                    </Column>
                    <Column lg={4} />
                  </>
                )}

                {searchBy.type === "date" && (
                  <>
                    <Column lg={3}>
                      <Field name="collectionDate">
                        {({ field }) => (
                          <TextInput
                            placeholder={"Collection Date(dd/mm/yyyy)"}
                            name={field.name}
                            id={field.name}
                            labelText={
                              <FormattedMessage id="search.label.collectiondate" />
                            }
                          />
                        )}
                      </Field>
                    </Column>
                    <Column lg={3}>
                      <Field name="recievedDate">
                        {({ field }) => (
                          <TextInput
                            placeholder={"Received Date(dd/mm/yyyy)"}
                            name={field.name}
                            id={field.name}
                            labelText={
                              <FormattedMessage id="search.label.recieveddate" />
                            }
                          />
                        )}
                      </Field>
                    </Column>
                    <Column lg={3}>
                      <Field name="testName">
                        {({ field }) => (
                          <Select
                            labelText={
                              <FormattedMessage id="search.label.test" />
                            }
                            name={field.name}
                            id={field.name}
                          >
                            <SelectItem text="" value="" />
                            {tests.map((test, index) => {
                              return (
                                <SelectItem
                                  key={index}
                                  text={test.value}
                                  value={test.id}
                                />
                              );
                            })}
                          </Select>
                        )}
                      </Field>
                    </Column>
                    <Column lg={3}>
                      <Field name="analysisStatus">
                        {({ field }) => (
                          <Select
                            labelText={
                              <FormattedMessage id="search.label.analysis" />
                            }
                            name={field.name}
                            id={field.name}
                          >
                            <SelectItem text="" value="" />
                            {analysisStatusTypes.map((test, index) => {
                              return (
                                <SelectItem
                                  key={index}
                                  text={test.value}
                                  value={test.id}
                                />
                              );
                            })}
                          </Select>
                        )}
                      </Field>
                    </Column>
                    <Column lg={3}>
                      <Field name="sampleStatusType">
                        {({ field }) => (
                          <Select
                            labelText={
                              <FormattedMessage id="search.label.sample" />
                            }
                            name={field.name}
                            id={field.name}
                          >
                            <SelectItem text="" value="" />
                            {sampleStatusTypes.map((test, index) => {
                              return (
                                <SelectItem
                                  key={index}
                                  text={test.value}
                                  value={test.id}
                                />
                              );
                            })}
                          </Select>
                        )}
                      </Field>
                    </Column>
                    <Column lg={1} />
                  </>
                )}

                {searchBy.type !== "patient" && searchBy.type !== "unit" && (
                  <Column lg={16}>
                    <Button
                      style={{ marginTop: "16px" }}
                      type="submit"
                      id="submit"
                    >
                      <FormattedMessage id="label.button.search" />
                    </Button>
                  </Column>
                )}
              </Grid>
            </Stack>
          </Form>
        )}
      </Formik>
      {searchBy.type === "patient" && (
        <SearchPatientForm
          getSelectedPatient={getSelectedPatient}
        ></SearchPatientForm>
      )}

      {searchBy.type === "unit" && (
        <>
          <Grid>
            <Column lg={6}>
              <Select
                labelText={<FormattedMessage id="search.label.testunit" />}
                name="unitType"
                id="unitType"
                onChange={submitOnSelect}
              >
                <SelectItem text="" value="" />
                {testSections.map((test, index) => {
                  return (
                    <SelectItem key={index} text={test.value} value={test.id} />
                  );
                })}
              </Select>
            </Column>
            <Column lg={10} />
          </Grid>
        </>
      )}
    </>
  );
}

export function SearchResults(props) {
  const { notificationVisible, setNotificationBody, setNotificationVisible } =
    useContext(NotificationContext);
  const { configurationProperties } = useContext(ConfigurationContext);

  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(100);
  const [acceptAsIs, setAcceptAsIs] = useState([]);
  const [referalOrganizations, setReferalOrganizations] = useState([]);
  const [methods, setMethods] = useState([]);
  const [referralReasons, setReferralReasons] = useState([]);
  const [rejectReasons, setRejectReasons] = useState([]);
  const saveStatus = "";

  const componentMounted = useRef(true);

  useEffect(() => {
    componentMounted.current = true;

    getFromOpenElisServer(
      "/rest/displayList/REFERRAL_ORGANIZATIONS",
      loadReferalOrganizations,
    );
    getFromOpenElisServer("/rest/displayList/METHODS", loadMethods);
    getFromOpenElisServer(
      "/rest/displayList/REFERRAL_REASONS",
      loadReferalReasons,
    );
    getFromOpenElisServer(
      "/rest/displayList/REJECTION_REASONS",
      loadRejectReasons,
    );
    return () => {
      componentMounted.current = false;
    };
  }, []);

  const loadReferalOrganizations = (values) => {
    if (componentMounted.current) {
      setReferalOrganizations(values);
    }
  };

  const loadMethods = (values) => {
    if (componentMounted.current) {
      setMethods(values);
    }
  };

  const loadReferalReasons = (values) => {
    if (componentMounted.current) {
      setReferralReasons(values);
    }
  };

  const loadRejectReasons = (values) => {
    if (componentMounted.current) {
      setRejectReasons(values);
    }
  };

  const addRejectResult = () => {
    const resultColumn = {
      name: "Reject",
      cell: (row, index, column, id) => {
        return renderCell(row, index, column, id);
      },
      width: "12rem",
    };

    if (configurationProperties.allowResultRejection == "true") {
      if (columns) {
        const updatedList = [
          ...columns.slice(0, 8),
          resultColumn,
          ...columns.slice(8),
        ];
        columns = updatedList;
      }
    }
  };

  var columns = [
    {
      name: "Sample Info",
      cell: (row, index, column, id) => {
        return renderCell(row, index, column, id);
      },
      sortable: true,
      width: "15rem",
    },
    {
      name: "Test Date",
      selector: (row) => row.testDate,
      sortable: true,
      width: "7rem",
    },

    {
      name: "Analyzer Result",
      selector: (row) => row.analysisMethod,
      sortable: true,
      width: "7rem",
    },
    {
      name: "Test Name",
      selector: (row) => row.testName,
      sortable: true,
      width: "8rem",
    },
    {
      name: "Normal Range",
      selector: (row) => row.normalRange,
      sortable: true,
      width: "8rem",
    },
    {
      name: "Accept",
      cell: (row, index, column, id) => {
        return renderCell(row, index, column, id);
      },
      width: "5rem",
    },
    {
      name: "Result",
      cell: (row, index, column, id) => {
        return renderCell(row, index, column, id);
      },
      width: "8rem",
    },
    {
      name: "Current Result",
      cell: (row, index, column, id) => {
        return renderCell(row, index, column, id);
      },
      width: "8rem",
    },
    {
      name: "Notes",
      cell: (row, index, column, id) => {
        return renderCell(row, index, column, id);
      },
      width: "10rem",
    },
  ];

  const renderCell = (row, index, column, id) => {
    let formatLabNum = configurationProperties.AccessionFormat === "ALPHANUM";

    console.log("renderCell: index: " + index + ", id: " + id);
    switch (column.name) {
      case "Sample Info":
        // return <input id={"results_" + id} type="text" size="6"></input>
        return (
          <>
            <div className="sampleInfo">
              <TextArea
                value={
                  (formatLabNum
                    ? convertAlphaNumLabNumForDisplay(row.accessionNumber)
                    : row.accessionNumber) +
                  "-" +
                  row.sequenceNumber +
                  "\n" +
                  row.patientName +
                  "\n" +
                  row.patientInfo
                }
                disabled={true}
                type="text"
                labelText=""
                rows={3}
              ></TextArea>
            </div>
            {row.nonconforming && (
              <picture>
                <img
                  src={config.serverBaseUrl + "/images/nonconforming.gif"}
                  alt="nonconforming"
                  width="20"
                  height="15"
                />
              </picture>
            )}
          </>
        );

      case "Accept":
        return (
          <>
            <Field name="forceTechApproval">
              {() => (
                <Checkbox
                  id={"testResult" + row.id + ".forceTechApproval"}
                  name={"testResult[" + row.id + "].forceTechApproval"}
                  labelText=""
                  //defaultChecked={acceptAsIs}
                  onChange={(e) => handleAcceptAsIsChange(e, row.id)}
                />
              )}
            </Field>
          </>
        );

      case "Reject":
        return (
          <>
            <Grid>
              <Column lg={16}>
                <Field name="reject">
                  {() => (
                    <Checkbox
                      id={"testResult" + row.id + ".rejected"}
                      name={"testResult[" + row.id + "].rejected"}
                      labelText=""
                      onChange={(e) => handleRejectCheckBoxChange(e, row.id)}
                    />
                  )}
                </Field>
              </Column>

              <Column lg={16}>
                <Select
                  id={"rejectReasonId" + row.id}
                  name={"testResult[" + row.id + "].rejectReasonId"}
                  //noLabel={true}
                  labelText={"Reason"}
                  onChange={(e) => handleChange(e, row.id)}
                >
                  {/* {...updateShadowResult(e, this, param.rowId)} */}
                  <SelectItem text="" value="" />
                  {rejectReasons.map((reason, reason_index) => (
                    <SelectItem
                      text={reason.value}
                      value={reason.id}
                      key={reason_index}
                    />
                  ))}
                </Select>
              </Column>
            </Grid>
          </>
        );

      case "Notes":
        return (
          <>
            <div className="note">
              <TextArea
                id={"testResult" + row.id + ".note"}
                name={"testResult[" + row.id + "].note"}
                //value={props.results.testResult[row.id]?.pastNotes}
                disabled={false}
                type="text"
                labelText=""
                rows={1}
                onChange={(e) => handleChange(e, row.id)}
              ></TextArea>
            </div>
          </>
        );

      case "Result":
        switch (row.resultType) {
          case "D":
            return (
              <Select
                className="result"
                id={"resultValue" + row.id}
                name={"testResult[" + row.id + "].resultValue"}
                noLabel={true}
                onChange={(e) => validateResults(e, row.id)}
              >
                {/* {...updateShadowResult(e, this, param.rowId)} */}
                <SelectItem text="" value="" />
                {row.dictionaryResults.map(
                  (dictionaryResult, dictionaryResult_index) => (
                    <SelectItem
                      text={dictionaryResult.value}
                      value={dictionaryResult.id}
                      key={dictionaryResult_index}
                    />
                  ),
                )}
              </Select>
            );

          case "N":
            return (
              <TextInput
                id={"ResultValue" + row.id}
                name={"testResult[" + row.id + "].resultValue"}
                //type="text"
                // value={resultForm.testResult[row.id].resultValue}
                labelText=""
                // helperText="Optional help text"
                onChange={(e) => handleChange(e, row.id)}
              />
            );

          case "R":
            return (
              <TextArea
                id={"ResultValue" + row.id}
                name={"testResult[" + row.id + "].resultValue"}
                style={{ width: "10px", height: "20px" }}
                //type="text"
                // value={resultForm.testResult[row.id].resultValue}
                labelText=""
                // helperText="Optional help text"
                onChange={(e) => handleChange(e, row.id)}
              />
            );

          case "A":
            return (
              <TextArea
                id={"ResultValue" + row.id}
                name={"testResult[" + row.id + "].resultValue"}
                style={{ width: "10px", height: "20px" }}
                //type="text"
                // value={resultForm.testResult[row.id].resultValue}
                labelText=""
                // helperText="Optional help text"
                onChange={(e) => handleChange(e, row.id)}
              />
            );

          default:
            return row.resultValue;
        }

      case "Current Result":
        switch (row.resultType) {
          case "D":
            return (
              <Select
                className="currentResult"
                id={"currentResultValue" + row.id}
                name={"testResult[" + row.id + "].resultValue"}
                noLabel={true}
                onChange={(e) => validateResults(e, row.id)}
                value={row.resultValue}
              >
                {/* {...updateShadowResult(e, this, param.rowId)} */}
                <SelectItem text="" value="" />
                {row.dictionaryResults.map(
                  (dictionaryResult, dictionaryResult_index) => (
                    <SelectItem
                      text={dictionaryResult.value}
                      value={dictionaryResult.id}
                      key={dictionaryResult_index}
                    />
                  ),
                )}
              </Select>
            );

          case "N":
            return (
              <TextInput
                id={"currentResultValue" + row.id}
                name={"testResult[" + row.id + "].resultValue"}
                //type="text"
                value={row.resultValue}
                labelText=""
                // helperText="Optional help text"
                onChange={(e) => handleChange(e, row.id)}
              />
            );

          case "R":
            return (
              <TextArea
                id={"ResultValue" + row.id}
                name={"testResult[" + row.id + "].resultValue"}
                style={{ width: "10px", height: "20px" }}
                //type="text"
                value={row.resultValue}
                labelText=""
                // helperText="Optional help text"
                onChange={(e) => handleChange(e, row.id)}
              />
            );

          case "A":
            return (
              <TextArea
                id={"ResultValue" + row.id}
                name={"testResult[" + row.id + "].resultValue"}
                style={{ width: "10px", height: "20px" }}
                //type="text"
                value={row.resultValue}
                labelText=""
                // helperText="Optional help text"
                onChange={(e) => handleChange(e, row.id)}
              />
            );

          default:
            return row.resultValue;
        }
      default:
        return;
    }
  };

  const renderReferral = ({ data }) => (
    <>
      <Grid>
        <Column lg={3}>
          <TextArea
            id={"testResult" + data.id + ".pastNotes"}
            name={"testResult[" + data.id + "].pastNotes"}
            value={data.pastNotes}
            disabled={true}
            type="text"
            labelText="Past Notes"
            rows={2}
          ></TextArea>
        </Column>
        <Column lg={2}>
          <Select
            id={"testMethod" + data.id}
            name={"testResult[" + data.id + "].testMethod"}
            labelText={"Methods"}
            onChange={(e) => handleChange(e, data.id)}
            value={data.method}
          >
            <SelectItem text="" value="" />
            {methods.map((method, method_index) => (
              <SelectItem
                text={method.value}
                value={method.id}
                key={method_index}
              />
            ))}
          </Select>
        </Column>
        <Column lg={3}>
          <Select
            id={"referralReason" + data.id}
            name={"testResult[" + data.id + "].referralReason"}
            // noLabel={true}
            labelText={"Referral Reason"}
            onChange={(e) => handleChange(e, data.id)}
          >
            {/* {...updateShadowResult(e, this, param.rowId)} */}
            <SelectItem text="" value="" />
            {referralReasons.map((reason, reason_index) => (
              <SelectItem
                text={reason.value}
                value={reason.id}
                key={reason_index}
              />
            ))}
          </Select>
        </Column>
        <Column lg={3}>
          <Select
            id={"institute" + data.id}
            name={"testResult[" + data.id + "].institute"}
            // noLabel={true}
            labelText={"Institute"}
            onChange={(e) => handleChange(e, data.id)}
          >
            {/* {...updateShadowResult(e, this, param.rowId)} */}

            <SelectItem text="" value="" />
            {referalOrganizations.map((org, org_index) => (
              <SelectItem text={org.value} value={org.id} key={org_index} />
            ))}
          </Select>
        </Column>
        <Column lg={3}>
          <Select
            id={"testToPerform" + data.id}
            name={"testResult[" + data.id + "].testToPerform"}
            // noLabel={true}
            labelText={"Test to Perform"}
            onChange={(e) => handleChange(e, data.id)}
          >
            {/* {...updateShadowResult(e, this, param.rowId)} */}

            <SelectItem text={data.testName} value={data.id} />
          </Select>
        </Column>
        <Column lg={2}>
          <DatePicker
            datePickerType="single"
            id={"sentDate_" + data.id}
            name={"testResult[" + data.id + "].sentDate_"}
            onChange={(date) => handleDatePickerChange(date, data.id)}
          >
            <DatePickerInput
              placeholder="mm/dd/yyyy"
              labelText="Sent Date"
              id="date-picker-single"
            />
          </DatePicker>
        </Column>
      </Grid>
    </>
  );

  const validateResults = (e, rowId) => {
    console.log("validateResults:" + e.target.value);
    // e.target.value;
    handleChange(e, rowId);
  };

  const handleChange = (e, rowId) => {
    const { name, id, value } = e.target;
    console.log("handleChange:" + id + ":" + name + ":" + value + ":" + rowId);
    // setState({value: e.target.value})
    // console.log('State updated to ', e.target.value);
    var form = props.results;
    var jp = require("jsonpath");
    jp.value(form, name, value);
    var isModified = "testResult[" + rowId + "].isModified";
    jp.value(form, isModified, "true");
  };

  const handleRejectCheckBoxChange = (e, rowId) => {
    const { name, checked } = e.target;
    var form = props.results;
    var jp = require("jsonpath");
    jp.value(form, name, checked);
    var shadowRejected = "testResult[" + rowId + "].shadowRejected";
    jp.value(form, shadowRejected, checked);
    var isModified = "testResult[" + rowId + "].isModified";
    jp.value(form, isModified, "true");
  };

  const handleDatePickerChange = (date, rowId) => {
    console.log("handleDatePickerChange:" + date);
    const d = new Date(date).toLocaleDateString("fr-FR");
    var form = props.results;
    var jp = require("jsonpath");
    jp.value(form, "testResult[" + rowId + "].sentDate_", d);
    var isModified = "testResult[" + rowId + "].isModified";
    jp.value(form, isModified, "true");
  };

  const handleAcceptAsIsChange = (e, rowId) => {
    console.log("handleAcceptAsIsChange:" + acceptAsIs[rowId]);
    handleChange(e, rowId);
    if (acceptAsIs[rowId] == undefined) {
      var message =
        `Checking this box will indicate that you accept the results unconditionally.\n` +
        `Expected uses:\n` +
        `1. The test has been redone and the result is the same.\n` +
        `2. There is no result for the test but you do not want to cancel it.\n` +
        `3. The result was changed and the technician wants to give the biologist the option to add a note during the validation step explaining the reason of the change.\n` +
        `In  either case, leave a note explaining why you are taking this action.\n`;

      // message=`Incorrect Username/Password Used \n Please try again…`

      alert(message);

      setNotificationBody({
        title: <FormattedMessage id="notification.title" />,
        message: message,
        kind: NotificationKinds.warning,
      });
      setNotificationVisible(true);
    }
    var newAcceptAsIs = acceptAsIs;
    newAcceptAsIs[rowId] = !acceptAsIs[rowId];
    setAcceptAsIs(newAcceptAsIs);
  };

  const handleSave = (values) => {
    //console.log("handleSave:" + values);
    values.status = saveStatus;
    var searchEndPoint = "/rest/ReactLogbookResultsUpdate";
    props.results.testResult.forEach((result) => {
      result.reportable = result.reportable === "N" ? false : true;
      delete result.result;
    });
    postToOpenElisServerJsonResponse(
      searchEndPoint,
      JSON.stringify(props.results),
      setResponse,
    );
  };

  const setResponse = (resp) => {
    console.log("setStatus" + JSON.stringify(resp));
    if (resp) {
      setNotificationBody({
        title: <FormattedMessage id="notification.title" />,
        message: createMesssage(resp),
        kind: NotificationKinds.success,
      });
    } else {
      setNotificationBody({
        title: <FormattedMessage id="notification.title" />,
        message: "Error while Saving",
        kind: NotificationKinds.error,
      });
    }
    setNotificationVisible(true);
  };

  const createMesssage = (resp) => {
    var message = "";
    if (resp.reflex.length > 0) {
      message += "Reflex Tests : " + resp.reflex.join(", ");
    }
    if (resp.calculated.length > 0) {
      message += "Calculated Tests : " + resp.calculated.join(", ");
    }
    if (message === "") {
      message += "Saved Succesfully";
    }
    return message;
  };

  const handlePageChange = (pageInfo) => {
    if (page != pageInfo.page) {
      setPage(pageInfo.page);
    }
    if (pageSize != pageInfo.pageSize) {
      setPageSize(pageInfo.pageSize);
    }
  };

  return (
    <>
      {notificationVisible === true ? <AlertDialog /> : ""}
      {addRejectResult()}
      <>
        {props.results.testResult.length > 0 && (
          <Grid style={{ marginTop: "20px" }} className="gridBoundary">
            <Column lg={3} />
            <Column lg={7}>
              <picture>
                <img
                  src={config.serverBaseUrl + "/images/nonconforming.gif"}
                  alt="nonconforming"
                  width="25" // Set your desired width
                  height="20" // Set your desired height
                />
              </picture>
              <b>
                {" "}
                <FormattedMessage id="validation.label.nonconform" />
              </b>
            </Column>
          </Grid>
        )}
        <Formik
          initialValues={SearchResultFormValues}
          //validationSchema={}
          onSubmit
          onChange
        >
          {({
            // values,
            // errors,
            // touched,
            handleChange,
            //handleBlur,
            // handleSubmit,
          }) => (
            <Form
              onChange={handleChange}
              //onBlur={handleBlur}
            >
              <DataTable
                data={props.results.testResult}
                columns={columns}
                isSortable
                expandableRows
                expandableRowsComponent={renderReferral}
              ></DataTable>
              <Pagination
                onChange={handlePageChange}
                page={page}
                pageSize={pageSize}
                pageSizes={[100, 50, 10]}
                totalItems={props.results.testResult?.length}
              ></Pagination>

              <Button type="button" id="submit" onClick={handleSave}>
                <FormattedMessage id="label.button.save" />
              </Button>
            </Form>
          )}
        </Formik>
      </>
    </>
  );
}

export default injectIntl(ResultSearchPage);