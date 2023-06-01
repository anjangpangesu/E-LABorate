const Joi = require('joi');

// Validate user input using Joi
const validateDiagnosisInput = (data) => {
    const schema = Joi.object({
      age: Joi.number()
        .integer()
        .min(1)
        .max(150)
        .required(),
      sex: Joi.string()
        .valid(0, 1)
        .required(),
      rbc: Joi.object({
        value: Joi.number().required(),
        units: Joi.string().valid('10¹² cells/L').required()
      }),
      hgb: Joi.object({
        value: Joi.number().required(),
        units: Joi.string().valid('g/dl').required()
      }),
      hct: Joi.object({
        value: Joi.number().required(),
        units: Joi.string().valid('%').required(),
      }),
      mcv: Joi.object({
        value: Joi.number().required(),
        units: Joi.string().valid('fL').required(),
      }),
      mch: Joi.object({
        value: Joi.number().required(),
        units: Joi.string().valid('pg').required(),
      }),
      mchc: Joi.object({
        value: Joi.number().required(),
        units: Joi.string().valid('g/dL').required(),
      }),
      rdw_cv: Joi.object({
        value: Joi.number().required(),
        units: Joi.string().valid('%').required(),
      }),
      wbc: Joi.object({
        value: Joi.number().required(),
        units: Joi.string().valid('10³ cells/cmm').required(),
      }),
      neu: Joi.object({
        value: Joi.number().required(),
        units: Joi.string().valid('%').required(),
      }),
      lym: Joi.object({
        value: Joi.number().required(),
        units: Joi.string().valid('%').required(),
      }),
      mo: Joi.object({
        value: Joi.number().required(),
        units: Joi.string().valid('%').required(),
      }),
      eos: Joi.object({
        value: Joi.number().required(),
        units: Joi.string().valid('%').required(),
      }),
      ba: Joi.object({
        value: Joi.number().required(),
        units: Joi.string().valid('%').required(),
      }),
    });

    return schema.validate(data);
  };

module.exports = {
  validateDiagnosisInput
};