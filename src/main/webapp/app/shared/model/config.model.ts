import { IConfValue } from 'app/shared/model/conf-value.model';

export interface IConfig {
    id?: number;
    key?: string;
    description?: string;
    des?: string;
    smartObjectId?: number;
    profileConfValueId?: number;
    confValues?: IConfValue[];
}

export class Config implements IConfig {
    constructor(
        public id?: number,
        public key?: string,
        public description?: string,
        public des?: string,
        public smartObjectId?: number,
        public profileConfValueId?: number,
        public confValues?: IConfValue[]
    ) {}
}
