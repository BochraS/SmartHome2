import { IConfig } from 'app/shared/model//config.model';
import { IProfile } from 'app/shared/model/profile.model';

export interface IProfileConfValue {
    id?: number;
    configs?: IConfig[];
    profiles?: IProfile[];
}

export class ProfileConfValue implements IProfileConfValue {
    constructor(public id?: number, public configs?: IConfig[], public profiles?: IProfile[]) {}
}
